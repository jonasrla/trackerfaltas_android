package lima.rocha.contadordefaltas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Index extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "INDEX";
    final private int ADICIONA_MATERIA = 0;
    final private int ADICIONA_FALTAS = 1;
    Toast chaveDuplicadas;

    private final String fileName;
    private final String backup;
    private GerenciaMateria gm;
    private TextView pontos;
    private AdapterListView adapter;

    public Index() {
        fileName = "contador_db.txt";
        backup = "bkp.txt";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        try {
            gm = extractData(fileName);
        } catch (Exception e){
            gm = extractData(backup);
        }
        pontos = (TextView) findViewById(R.id.Pontos);
        TextView adicionaMateria = (TextView) findViewById(R.id.AdicionaMateria);
        adicionaMateria.setOnClickListener(this);

        adapter = new AdapterListView(this,
                R.layout.item_list,  populateList());


        ListView listaMateria = (ListView) findViewById(R.id.ListaMateria);
        listaMateria.setAdapter(adapter);
        listaMateria.setItemsCanFocus(true);
        chaveDuplicadas = Toast.makeText(this, "Materias Duplicadas",Toast.LENGTH_LONG);

        listaMateria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "selected: " + position);
            }
        });
        updatePontos();

    }

    public void removeMateria(final View view){
        Log.v(TAG, "Funcionou!" + Integer.toString(view.getId()));
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Deletar Matéria");
        builder.setMessage("Deseja mesmo deletar?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ItemListLite removido = adapter.getItem((int) view.getTag());
                gm.removeMateria(removido.getMateria());
                adapter.remove(removido);
                updatePontos();
                save();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        builder.setCancelable(true);
        builder.setIcon(R.drawable.warning);
        builder.show();
    }

    private void updatePontos(){
        int p = gm.getTotalDePontos();
        pontos.setText(Integer.toString(p));
        if (p < 60){
            pontos.setTextColor(getResources().getColor(R.color.deepGreen));
        } else if(p<90){
            pontos.setTextColor(getResources().getColor(R.color.deepYellow));
        } else {
            pontos.setTextColor(getResources().getColor(R.color.deepRed));
        }
    }

    private ArrayList<ItemListLite> populateList(){
        ArrayList<ItemListLite> l = new ArrayList<>();
        for (String nome:gm.getNomes()){
            l.add(new ItemListLite(nome, Integer.toString(gm.getPorcentagem(nome))+"%",
                    gm.getDistribution(nome)));

        }
        return l;
    }

    public void adicionaFalta(View view){
        String materia = adapter.getItem((int) view.getTag()).getMateria();
        Intent FaltaIntent = new Intent(this, AdicionaFalta.class);
        int totalAulas = gm.getTotalAulas(materia);
        int totalFaltas = gm.getTotalFaltas(materia);
        FaltaIntent.putExtra("total", totalAulas);
        FaltaIntent.putExtra("faltas", totalFaltas);
        FaltaIntent.putExtra("materia", materia);
        startActivityForResult(FaltaIntent, ADICIONA_FALTAS);

    }


    private GerenciaMateria extractData(String f){
        try {
            FileInputStream fis = openFileInput(f);
            int bufSize = fis.available();
            byte []b = new byte[bufSize];
            String buf = "";
            while (bufSize != 0) {
                fis.read(b);
                buf += new String(b, "UTF-8");
                bufSize = fis.available();
                b = new byte[bufSize];
            }
            return new GerenciaMateria(buf);
        } catch (FileNotFoundException e) {
            return new GerenciaMateria();
        } catch (IOException e) {
            return new GerenciaMateria();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case (ADICIONA_MATERIA):
                    try {
                        String nome = data.getStringExtra("nome");
                        int total = data.getIntExtra("total", 0);

                        Log.v(TAG, "Adicionando Materia :" + nome + " " + Integer.toString(total));

                        gm.addMateria(nome, total);

                        adapter.add(new ItemListLite(nome, Integer.toString(gm.getPorcentagem(nome)) + "%",
                                gm.getDistribution(nome)));
                        adapter.notifyDataSetChanged();

                        save();
                    } catch (DuplicateKeyException e) {
                        Log.e(TAG, e.getMessage());
                        chaveDuplicadas.show();
                    }
                    break;
                case (ADICIONA_FALTAS):

                    String materia = data.getStringExtra("materia");
                    int faltas = data.getIntExtra("faltas", 0);
                    int faltasJustificadas = data.getIntExtra("faltasJustificadas", 0);

                    Log.d(TAG, "Adiciona faltas " + Integer.toString(faltas) + " faltas justificadas " +
                    Integer.toString(faltasJustificadas) + " à " + materia);

                    gm.addFaltas(materia, faltas, faltasJustificadas);

                    adapter.getItem(materia).setDistribuicao(gm.getDistribution(materia));
                    adapter.getItem(materia).setPercentagem(Integer.toString(
                            gm.getPorcentagem(materia)) + "%");

                    updatePontos();
                    adapter.notifyDataSetChanged();

                    save();
                    break;
                default:
                    break;
            }
        }
    }




    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.AdicionaMateria:
                Intent AdicionaIntent = new Intent(this, AdicionaMateria.class);
                String nomes[] = gm.getNomes().toArray(new String[gm.size()]);
                Log.d(TAG, "Listando nomes:");
                for (String n:nomes)
                    Log.d(TAG, "   "+n);
                AdicionaIntent.putExtra("usedNames", nomes);
                startActivityForResult(AdicionaIntent, ADICIONA_MATERIA);
                break;

            default:
                break;
        }

    }

    protected void save(){
        String finalState = gm.serialize();
        File file = new File(this.getFilesDir(), fileName);
        if (file.exists()){
            file.renameTo(new File(this.getFilesDir(), backup));
            file = new File(this.getFilesDir(), fileName);
        }
        try {
            file.createNewFile();
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(finalState.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Não conseguiu abrir o arquivo");
        } catch (IOException e) {
            Log.e(TAG, "Não conseguiu escrever o arquivo");
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        save();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
}
