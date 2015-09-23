package lima.rocha.contadordefaltas;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GerenciaMateria {
    final private String TAG = "GERENCIA";
    private Map<String,Materia> materias;
    private String lastMateria;
    private int [] lastValues;

    public GerenciaMateria(){
        materias = new HashMap<>();
        lastMateria = "";
        lastValues = new int[]{};
    }

    public GerenciaMateria(String buf) {
        String []lines = buf.split("\n");
        materias = new HashMap<>();
        for (String line : lines) {
            materias.put(line.substring(0, line.indexOf(",")), new Materia(line));
        }
    }

    public void addMateria(String nome, int totalDeAulas) throws DuplicateKeyException {
        if (!materias.containsKey(nome))
            materias.put(nome, new Materia(nome, totalDeAulas));
        else {
            throw new DuplicateKeyException("Materia jah existe", new Throwable(nome));
        }
    }

    public void removeMateria(String nome){
        materias.remove(nome);
    }

    public void addFaltas(String materia, int faltas, int faltasJustificadas){
        Materia m = materias.get(materia);
        int f = m.getFaltas();
        int fj = m.getFaltasJustificadas();
        try{
            m.addFaltas(faltas);
            m.addFaltasJustificadas(faltasJustificadas);
        } catch (Exception e){
            Log.e(TAG,Log.getStackTraceString(e));
            m.returnState(f,fj);
        }
    }

    public void undo(){
        Materia m = materias.get(lastMateria);
        m.returnState(lastValues[0],lastValues[1]);
    }

    public int getTotalDePontos(){
        int total = 0;
        for (Materia m: materias.values()){
            total += m.getFaltas()*3 + m.getFaltasJustificadas();
        }
        return total;
    }

    public Set<String> getNomes(){
        return materias.keySet();
    }

    public int getPorcentagem(String materia){
        try {
            return materias.get(materia).getPercentualDeFaltas();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getDistribution(String materia){
        return Integer.toString(materias.get(materia).getFaltas())+"/"+
                Integer.toString(materias.get(materia).getFaltasJustificadas());
    }

    public String serialize(){
        String output = "";
        for (Materia materia : materias.values()) {
            output += materia.serialize() + "\n";
        }
        return output;
    }

    public int getTotalAulas(String materia){
        return materias.get(materia).getTotalDeAulas();
    }

    public int getTotalFaltas(String materia){
        return materias.get(materia).getTotalDeFaltas();
    }

    public int size(){
        return materias.size();
    }



}
