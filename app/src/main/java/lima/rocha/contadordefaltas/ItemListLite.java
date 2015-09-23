package lima.rocha.contadordefaltas;


public class ItemListLite {
    final private String Materia;
    private String Percentagem;
    private String Distribuicao;

    public ItemListLite(String materia, String percentagem, String distribuicao){
        Materia = materia;
        Percentagem = percentagem;
        Distribuicao = distribuicao;
    }



    public void setPercentagem(String p){
        Percentagem = p;
    }

    public String getPercentagem(){
        return Percentagem;
    }

    public void setDistribuicao(String d){
        Distribuicao = d;
    }

    public String getDistribuicao(){
        return Distribuicao;
    }

    public String getMateria(){
        return Materia;
    }
}
