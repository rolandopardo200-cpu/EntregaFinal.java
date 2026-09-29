
public class Vendedor {

    private String tipoDocumento;
    private long numeroDocumento;
    private String nombres;
    private String apellidos;
    private double dineroRecaudado;

 
    public Vendedor(
            String tipoDocumento,
            long numeroDocumento,
            String nombres,
            String apellidos) {

        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dineroRecaudado = 0;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public long getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public double getDineroRecaudado() {
        return dineroRecaudado;
    }

    
    public void agregarRecaudo(double dinero) {
        dineroRecaudado += dinero;
    }

   
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}