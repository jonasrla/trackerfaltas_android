package lima.rocha.contadordefaltas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdicionaFalta extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    final private String TAG = "FALTA";
    private EditText faltas;
    private EditText faltasJustificadas;
    private int totalAulas;
    private int totalFaltas;
    private String materia;

    private Toast maiorQueNAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_falta);

        totalAulas = getIntent().getIntExtra("total", 0);
        totalFaltas = getIntent().getIntExtra("faltas", 0);

        TextView titulo = (TextView) findViewById(R.id.MateriaFalta);
        materia = getIntent().getStringExtra("materia");
        titulo.setText(materia);

        faltas = (EditText) findViewById(R.id.faltas);

        faltasJustificadas = (EditText) findViewById(R.id.faltasJustificadas);

        Button adiciona = (Button) findViewById(R.id.AdicionaFalta);
        adiciona.setOnClickListener(this);

        maiorQueNAulas = Toast.makeText(this, "As faltas excedem o total de aulas", Toast.LENGTH_LONG);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adiciona_falta, menu);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int f = 0;
        int fj = 0;
        try {
            f = Integer.valueOf(faltas.getText().toString());
            fj = Integer.valueOf(faltasJustificadas.getText().toString());
        } catch (NumberFormatException e){
            Log.v(TAG, "Blank valued");
        }
        if (f + fj + totalFaltas > totalAulas && totalAulas != 0) {
            maiorQueNAulas.show();
        } else {
            Intent data = new Intent();
            data.putExtra("faltas", f);
            data.putExtra("faltasJustificadas", fj);
            data.putExtra("materia", materia);
            setResult(RESULT_OK, data);
            finish();
        }
    }


    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            v.setSelected(true);
        }
    }
}
