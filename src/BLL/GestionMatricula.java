package BLL;

import BEU.Curso;
import BEU.Estado;
import BEU.Estudiante;
import BEU.Matricula;
import Rugel.BaseBllCrud;
import Rugel.BasePersistencia;
import Rugel.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestionMatricula extends BasePersistencia<Matricula> implements BaseBllCrud<Matricula> {
    
    private Matricula matricula;
    private final String directorio = "Matriculas";
    
    public GestionMatricula() {
    }
    
    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }
    
  
    
    public String calificar(float valor) {
        String mensaje;
        int num = this.matricula.addCalificacion(valor);
        switch (num) {
            case 0:
                mensaje = "Todas las notas están registradas";
                break;
            case 1:
                mensaje = "Calificación de la Unidad I registrada corectamente.\n";
                break;
            case 2:
                mensaje = "Calificación de la Unidad II registrada corectamente.\n";
                break;
            case 3:
                mensaje = "Calificación de la Unidad III registrada corectamente.\n";
                break;
            default:
                mensaje = "Hubo un error al registrar la calificación.\n";
                break;
        }
        return mensaje;
    }
    
    public void promediar() {
        this.matricula.CalcularPromedio();
    }
    
    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estudiante: ");
        sb.append(matricula.getEstudiante()).append("\n");
        sb.append("Curso: ");
        sb.append(matricula.getCurso().getTitulo()).append("\n");
        sb.append("Promedio: ");
        sb.append(matricula.getPromedio());
        sb.append("\n");
        sb.append(matricula.imprimirDetalle()).append("\n");
        return sb.toString();
    }
    
    public String imprimirDetalleTotal() {
        return matricula.imprimirDetalleTotal();
    }
    
    public void anular(boolean op) {
        if (op == true) {
            matricula.setEstado(Estado.ANULADO);
            Util.imprimir("\nSu matricula a sido anulada correctamente ");
            float valor = matricula.getCurso().getCosto() * 0.10f;
            Util.imprimir("Usted debe pagar el 10% por el rugro:  $" + valor);
        }
    }
    
    public void archivar() throws IOException {
        this.escribir(directorio, this.matricula.getNumero(), matricula);
    }
    
    public void configurar(Curso cr, Estudiante est) {
        this.matricula.setCurso(cr);
        this.matricula.setEstudiante(est);
    }
    
    
    public List<Matricula> reportar(String titulo) throws IOException{
      List <Matricula> resultado = new ArrayList <> ();
      List<String> contenidos = this.leerdirectorio(directorio , titulo);
      for(String texto : contenidos){
           GsonBuilder gb = new GsonBuilder();
        gb.setPrettyPrinting();
        Gson gson = gb.create();
        try{
            Matricula m = gson.fromJson(texto, Matricula.class);
            resultado.add(m);
            
        }
        catch(JsonSyntaxException ex){
            Util.imprimir("El texto no se pudo convertir en matricula\n");
            Util.imprimir(ex.toString()+"\n");
        }
       
      }
      return resultado;
    }
   

    @Override
    public void crear() {
        matricula=new Matricula();
    }

    @Override
    public void consultar(String id) throws IOException {
         String archivar = id +".json";
        String contenido = this.leer(directorio, archivar);
       
         GsonBuilder gb = new GsonBuilder();
        gb.setPrettyPrinting();
        Gson gson = gb.create();
        matricula = gson.fromJson(contenido, Matricula.class);
    }

    @Override
    public void actualizar(Matricula t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminiar(Matricula t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
