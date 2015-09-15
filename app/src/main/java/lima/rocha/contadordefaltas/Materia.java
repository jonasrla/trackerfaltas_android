package lima.rocha.contadordefaltas;

public class Materia {
    final private String nome;
    private int faltas, faltasJustificadas, totalDeAulas;

    public Materia(String nome1, int tda) {
        nome = nome1;
        totalDeAulas = tda;
    }

    public Materia(String serial){
        String []splinted = serial.split(",");
        nome = splinted[0];
        faltas = Integer.valueOf(splinted[1]);
        faltasJustificadas = Integer.valueOf(splinted[2]);
        totalDeAulas = Integer.valueOf(splinted[3]);
    }

    public void returnState(int faltas1, int faltasJustificadas1){
        faltas = faltas1;
        faltasJustificadas = faltasJustificadas1;
    }

    public void addFaltas(int afaltas) throws Exception {
        if (totalDeAulas != 0 && (faltas+afaltas+faltasJustificadas) < totalDeAulas) {
            faltas += afaltas;
        } else {
            throw new Exception("Erro na adicao: Extrapolou o total de aulas");
        }
    }

    public void addFaltasJustificadas(int ajfaltas) throws Exception {
        if (totalDeAulas != 0 && (faltas+ajfaltas+faltasJustificadas) < totalDeAulas) {
            faltasJustificadas += ajfaltas;
        } else {
            throw new Exception("Erro na adicao: Extrapolou o total de aulas");
        }
    }

    public float getPercentualDeFaltas() throws Exception {
        if (totalDeAulas != 0)
            return (float) (faltas + faltasJustificadas) / totalDeAulas;
        else
            throw new Exception("Total de aulas não definidos");
    }

    public int[] getEstado(){
        return new int[]{faltas, faltasJustificadas};
    }


    public String serialize(){
        return nome+","+Integer.toString(faltas)+","+Integer.toString(faltasJustificadas)
        +","+Integer.toString(totalDeAulas);
    }
}
