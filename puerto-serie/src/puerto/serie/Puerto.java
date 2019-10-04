package puerto.serie;

import javax.comm.CommPortIdentifier;

public class Puerto {

    private CommPortIdentifier puertoSerie;
    private String nombre;
    
    public Puerto() {
        this.puertoSerie = null;
        this.nombre = null;
    }

    public Puerto(CommPortIdentifier puertoSerie, String nombre) {
        this.puertoSerie = puertoSerie;
        this.nombre = nombre;
    }
    
    public CommPortIdentifier getPuertoSerie() {
        return puertoSerie;
    }

    public void setPuertoSerie(CommPortIdentifier puertoSerie) {
        this.puertoSerie = puertoSerie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        
}
