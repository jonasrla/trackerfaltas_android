package lima.rocha.contadordefaltas;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class Index extends AppCompatActivity implements View.OnClickListener {
    private final String fileName;
    private GerenciaMateria gm;
    private TextView pontos;
    private TextView adicionaMateria;
    private LinearLayout listaMateria;
    private Map<String,Integer> myBoxes;

    public Index() {
        fileName = "contador_db.txt";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        gm = getGerenciaMateria();
        pontos = (TextView) findViewById(R.id.Pontos);
        adicionaMateria = (TextView) findViewById(R.id.AdicionaMateria);
        adicionaMateria.setOnClickListener(this);
//        listaMateria = (LinearLayout) findViewById(R.id.ListaMateria);
        updatePontos();
    }

    private void updatePontos(){
        int p = gm.getTotalDePontos();
        pontos.setText(Integer.toString(p));
        if (p < 60){
            pontos.setTextColor(Color.GREEN);
        } else if(p<90){
            pontos.setTextColor(Color.YELLOW);
        } else {
            pontos.setTextColor(Color.RED);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addMateriaToList(String nome){
        LinearLayout box = new LinearLayout(this);
        TextView materia = new TextView(this);
        TextView perc = new TextView(this);
        TextView faltaDist = new TextView(this);
        int p = (int) (100*gm.getPorcentagem(nome));
        int dist[] = gm.getFaltasDistribution(nome);
        int id;

        materia.setText(nome);
        materia.setClickable(true);
        materia.setOnClickListener(this);

        perc.setText(Integer.toString(p) + "%");
        if(p<12){
            perc.setTextColor(Color.GREEN);
        } else if(p<19){
            perc.setTextColor(Color.YELLOW);
        } else {
            perc.setTextColor(Color.YELLOW);
        }

//        faltaDist.setText('');


        id = View.generateViewId();
        box.setId(id);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setClickable(true);
        box.setOnClickListener(this);


        myBoxes.put(nome,id);
    }

    private GerenciaMateria getGerenciaMateria(){
        try {
            FileInputStream fis = openFileInput(fileName);
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
    public void onClick(View v) {

    }
}
