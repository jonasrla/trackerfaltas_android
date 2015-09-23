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
import android.widget.Toast;

public class AdicionaMateria extends AppCompatActivity implements View.OnClickListener {
    final private String TAG = "ADICIONA";

    private EditText NomeMateria;
    private EditText TotalDeAulas;
    private String[] UsedNames;
    private Toast voidName;
    private Toast nameUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_materia);
        NomeMateria = (EditText) findViewById(R.id.NomeMateria);
        TotalDeAulas = (EditText) findViewById(R.id.TotalDeAulas);
        Button adiciona = (Button) findViewById(R.id.Adiciona);
        adiciona.setOnClickListener(this);
        UsedNames = getIntent().getStringArrayExtra("usedNames");

        voidName = Toast.makeText(this, "Defina um Nome", Toast.LENGTH_LONG);
        nameUsed = Toast.makeText(this, "Materia ja foi adicionada", Toast.LENGTH_LONG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adiciona_materia, menu);
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

    public boolean checkName(String Name){
        if (UsedNames != null) {
            Log.v(TAG,"UsedNames nao eh nulo");
            for (String name : UsedNames) {
                if (name.equals(Name)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        String name = NomeMateria.getText().toString();
        if (name.isEmpty()){
            voidName.show();
        } else if (checkName(name)) {
            Intent data = new Intent();
            int total = Integer.valueOf(TotalDeAulas.getText().toString());
            data.putExtra("nome",name);
            data.putExtra("total",total);
            setResult(RESULT_OK,data);
            finish();
        } else {
            nameUsed.show();
        }
    }
}
