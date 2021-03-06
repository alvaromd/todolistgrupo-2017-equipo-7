package models;

import javax.persistence.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import play.data.format.*;
import java.lang.String;
import java.util.Set;
import java.util.HashSet;

import java.util.Set;
import java.util.HashSet;
import java.sql.Timestamp;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;

@Entity
public class Tarea {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private Long id;
   private String titulo;
   @Formats.DateTime(pattern="yyyy-MM-dd")
   @Temporal(TemporalType.DATE)
   private Date fechaCreacion;
   @Formats.DateTime(pattern="yyyy-MM-dd")
   @Temporal(TemporalType.DATE)
   private Date fechaLimite;
   @Formats.DateTime(pattern="yyyy-MM-dd")
   @Temporal(TemporalType.DATE)
   private Date deleted_at;
   private boolean terminada;
   private String descripcion;
   // Relación muchos-a-uno entre tareas y usuario
   @ManyToOne
   // Nombre de la columna en la BD que guarda físicamente
   // el ID del usuario con el que está asociado una tarea
   @JoinColumn(name="usuarioId")
   public Usuario usuario;

   @ManyToMany(fetch=FetchType.EAGER)
   @JoinTable(name="Usuario_Tarea")
   public Set<Usuario> usuariosAsignados = new HashSet<Usuario>();

   // Relación muchos-a-uno entre tareas y columna
   @ManyToOne
   // Nombre de la columna en la BD que guarda físicamente
   // el ID del usuario con el que está asociado una tarea
   @JoinColumn(name="columnaId")
   public Columna columna;

   @ManyToMany(fetch=FetchType.EAGER)
   @JoinTable(name="Tarea_Etiqueta")
   public Set<Etiqueta> etiquetas = new HashSet<Etiqueta>();

   public Tarea() {}

   public Tarea(Usuario usuario, String titulo, Columna columna) {
      this.usuario = usuario;
      this.titulo = titulo;
      this.terminada = false;
      this.fechaCreacion = new Date();
      this.fechaLimite = null;
      this.deleted_at = null;
      this.descripcion = "";
      this.columna = columna;
   }

   // Getters y setters necesarios para JPA

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }

   public Columna getColumna() {
      return columna;
   }

   public void setColumna(Columna columna) {
      this.columna = columna;
   }

   public Date getFechaCreacion() { return fechaCreacion; }

   public Date getFechaLimite() { return fechaLimite; }

   public void setFechaLimite(Date fechaLimite) { this.fechaLimite = fechaLimite; }

   public Date getDeletedAt() {
     return this.deleted_at;
   }

   public void setDeletedAt(Date deleted_at) {
     this.deleted_at = deleted_at;
   }

   public boolean getTerminada() { return this.terminada; }

   public void setTerminada(boolean terminada) { this.terminada = terminada; }

   public String getDescripcion() { return this.descripcion; }

   public void setDescripcion(String descripcion) { this.descripcion = descripcion; }


   public Set<Usuario> getUsuariosAsignados() {
     return this.usuariosAsignados;
   }

   public void setUsuariosAsignados(Set<Usuario> usuariosAsignados) {
     this.usuariosAsignados = usuariosAsignados;

   }

   public Set<Etiqueta> getEtiquetas() {
      return etiquetas;
   }

   public void setEtiquetas(Set<Etiqueta> etiquetas) {
      this.etiquetas = etiquetas;
   }

   public String toString() {
      return String.format("Tarea id: %s titulo: %s usuario: %s",
                      id, titulo, usuario.toString());
   }

   public String toJSON() {
     String creacion = new SimpleDateFormat("yyyy-MM-dd").format(fechaCreacion);
     String limite = ((fechaLimite == null) ? "0000-00-00" : new SimpleDateFormat("yyyy-MM-dd").format(fechaLimite));

      ObjectMapper mapper = new ObjectMapper();
      mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      ObjectNode node = mapper.getNodeFactory().objectNode();
      node.put("id", id);
      node.put("titulo", titulo);
      node.put("fechaCreacion", creacion);
      node.put("fechaLimite", limite);
      node.put("descripcion", descripcion);
      node.put("terminada", terminada);
      try {
        String tareaString = mapper.writeValueAsString(node);
        return tareaString;
      } catch(Exception e) {
        System.out.println(e);
      }
      return "";
      //String creacion = new SimpleDateFormat("yyyy-MM-dd").format(fechaCreacion);
      //String limite = ((fechaLimite == null) ? "0000-00-00" : new SimpleDateFormat("yyyy-MM-dd").format(fechaLimite));
      //return String.format("{\"id\":\"%s\", \"titulo\":\"%s\", \"fechaCreacion\":\"%s\", \"fechaLimite\":\"%s\", \"descripcion\":\"%s\", \"terminada\": %s }",
      //                id, titulo, creacion, limite, descripcion, terminada);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((titulo == null) ? 0 : titulo.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Tarea other = (Tarea) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
      return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (titulo == null) {
            if (other.titulo != null) return false;
         } else if (!titulo.equals(other.titulo)) return false;
         if (usuario == null) {
            if (other.usuario != null) return false;
            else if (!usuario.equals(other.usuario)) return false;
         }
      }
      return true;
   }
}
