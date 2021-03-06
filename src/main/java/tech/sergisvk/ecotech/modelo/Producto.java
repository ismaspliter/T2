package tech.sergisvk.ecotech.modelo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;


@Entity
public class Producto {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Hace uso de la implementación isValid() de la clase @NotNull, además identidica que el tamaño/longitud del objeto
     * suministrado es mayor a 1 y menor a 31.
     */
    @NotEmpty
    @Size(min = 1, max = 32)
    private String nombre;

    /**
     * Es un tipo de datos para almacenar datos de objetos grandes. Siendo BLOB y CLOB.
     * BLOB es para almacenar datos binarios y CLOB para almacenar datos de texto.
     * Siendo la descripción del producto, nos interesa guardar datos de texto. ASí que usaremos CLO.
     */
    @Lob
    @Column(columnDefinition="VARCHAR(128)")
    private String descripcion;

    @Min(0)
    private float pvp; //Precio de venta al público, siendo un minimo del 0

    private float descuento;

    @URL
    private String imagen;

    /**
     * No puede ser nulo, la anotación se define en la especificación Verification Bean. Esto significa que no se
     * limita solo a las entidad. Por lo contrario también podemos usar el @NotNull en cualquier otro bean.
     */
    @NotNull
    /* 1:n Uno a muchos
     * La forma de funcionar es a nivel de base de datos es que tenemos una id clave principal en tabla de Productos
     * y tambien una id como clave externa en la tabla de puntacion.
     */
    @ManyToOne
    private Categoria categoria;

    /**
     * Todo produto tendrá un propietario
     */
    @ManyToOne
    private Usuario propietario;

    /**
     * Un producto podrá estar comprado mediante una compra
     */
    @ManyToOne
    Compra compra;

    /**
     * 1:n
     */
    @OneToMany(mappedBy="producto", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    private Set<Puntuacion> puntuaciones = new HashSet<Puntuacion>();

    public Producto() {
        //void
    }



    /**
     * Información que necesitamos saber sobre el producto
     * @param nombre que vaya a recibir
     * @param descripcion detalles y caracteríticas
     * @param pvp que tipo de impuesto se le aplica
     * @param descuento si esta de oferta o no con la cantidad a descuento
     * @param imagen URL de la imagen
     * @param categoria donde seguarda
     * @param propietario nombre del propietario
     */
    public Producto(String nombre, String descripcion, float pvp, float descuento, String imagen, Categoria categoria,
                    Usuario propietario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pvp = pvp;
        this.descuento = descuento;
        this.imagen = imagen;
        this.categoria = categoria;
        this.propietario = propietario;
    }


    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public Categoria getCategoria() {
        return categoria;
    }


    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(Set<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    /**
     * Métodos helper
     */
    public void addPuntuacion(Puntuacion puntuacion) {
        this.puntuaciones.add(puntuacion);
        puntuacion.setProducto(this);
    }


    public double getPuntuacionMedia() {
        if (this.puntuaciones.isEmpty())
            return 0;
        else
            return this.puntuaciones.stream()
                    .mapToInt(Puntuacion::getPuntuacion)
                    .average()
                    .getAsDouble();
    }

    public double getNumeroTotalPuntuaciones() {
        return this.puntuaciones.size();
    }


}