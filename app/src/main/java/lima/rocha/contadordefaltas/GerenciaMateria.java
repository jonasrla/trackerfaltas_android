package lima.rocha.contadordefaltas;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GerenciaMateria {
    private int totalDePontos;
    private Map<String,Materia> materias;
    private String lastMateria;
    private int [] lastValues;

    public GerenciaMateria(){
        totalDePontos = 0;
        materias = new HashMap<>();
        lastMateria = "";
        lastValues = new int[]{};
    }

    public GerenciaMateria(String buf) {
        String []lines = buf.split("\n");
        totalDePontos = Integer.valueOf(lines[0]);
        for (int i=1; i<lines.length; i++)
            materias.put(lines[i].substring(0, lines[i].indexOf(",")), new Materia(lines[i]));
    }

    public void addMateria(String nome, int totalDeAulas) throws Exception {
        if (!materias.containsKey(nome))
            materias.put(nome, new Materia(nome, totalDeAulas));
        else {
            throw new Exception("Erro adicao de materia: Materia ja existe");
        }
    }

    public void removeMateria(String nome){
        materias.remove(nome);
    }

    public void addFaltas(String materia, int faltas, int faltasJustificadas){
        Materia m = materias.get(materia);
        int []estado = m.getEstado();
        try{
            m.addFaltas(faltas);
            m.addFaltasJustificadas(faltasJustificadas);
            totalDePontos = faltas*3 + faltasJustificadas;
        } catch (Exception e){
            m.returnState(estado[0],estado[1]);
        }
    }

    public void undo(){
        Materia m = materias.get(lastMateria);
        totalDePontos -= (lastValues[0] * 3) + lastValues[1];
        m.returnState(lastValues[0],lastValues[1]);
    }

    public int getTotalDePontos(){
        return totalDePontos;
    }

    public Set<String> getNomes(){
        return materias.keySet();
    }

    public float getPorcentagem(String materia){
        try {
            return materias.get(materia).getPercentualDeFaltas();
        } catch (Exception e) {
            return 0.0f;
        }
    }

    private String serialize(){
        String output = Integer.toString(totalDePontos)+"\n";
        for (Materia materia : materias.values()) {
            output += materia.serialize() + "\n";
        }
        return output;
    }

    public int[] getFaltasDistribution(String materia){
        return materias.get(materia).getEstado();
    }

}
