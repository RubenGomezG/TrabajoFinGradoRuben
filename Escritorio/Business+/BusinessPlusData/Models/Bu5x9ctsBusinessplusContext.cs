using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Pomelo.EntityFrameworkCore.MySql.Scaffolding.Internal;

namespace BusinessPlusData.Models;

public partial class Bu5x9ctsBusinessplusContext : DbContext
{
    public Bu5x9ctsBusinessplusContext()
    {
    }

    public Bu5x9ctsBusinessplusContext(DbContextOptions<Bu5x9ctsBusinessplusContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Academia> Academias { get; set; }

    public virtual DbSet<Conversacione> Conversaciones { get; set; }

    public virtual DbSet<Curso> Cursos { get; set; }

    public virtual DbSet<Favorito> Favoritos { get; set; }

    public virtual DbSet<Inscripcione> Inscripciones { get; set; }

    public virtual DbSet<Mensaje> Mensajes { get; set; }

    public virtual DbSet<Usuario> Usuarios { get; set; }

   

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder
            .UseCollation("utf8mb4_0900_ai_ci")
            .HasCharSet("utf8mb4");

        modelBuilder.Entity<Academia>(entity =>
        {
            entity.HasKey(e => e.CodAcademia).HasName("PRIMARY");

            entity.ToTable("academias");

            entity.HasIndex(e => e.Email, "email").IsUnique();

            entity.Property(e => e.CodAcademia).HasColumnName("cod_academia");
            entity.Property(e => e.Contrasena)
                .HasColumnType("text")
                .HasColumnName("contrasena");
            entity.Property(e => e.Direccion)
                .HasColumnType("text")
                .HasColumnName("direccion");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .HasColumnName("email");
            entity.Property(e => e.ImgPerfil)
                .HasMaxLength(100)
                .HasColumnName("img_perfil");
            entity.Property(e => e.Latitud).HasColumnName("latitud");
            entity.Property(e => e.Longitud).HasColumnName("longitud");
            entity.Property(e => e.Nombre)
                .HasMaxLength(20)
                .HasColumnName("nombre");
            entity.Property(e => e.Telefono).HasColumnName("telefono");
            entity.Property(e => e.Usuario)
                .HasMaxLength(50)
                .HasColumnName("usuario");
        });

        modelBuilder.Entity<Conversacione>(entity =>
        {
            entity.HasKey(e => e.CodConversacion).HasName("PRIMARY");

            entity.ToTable("conversaciones");

            entity.HasIndex(e => new { e.Usuario1Id, e.Usuario2Id }, "unica_conversacion").IsUnique();

            entity.HasIndex(e => e.Usuario2Id, "usuario2_id");

            entity.Property(e => e.CodConversacion).HasColumnName("cod_conversacion");
            entity.Property(e => e.Usuario1Id)
                .HasMaxLength(50)
                .HasColumnName("usuario1_id");
            entity.Property(e => e.Usuario2Id)
                .HasMaxLength(50)
                .HasColumnName("usuario2_id");

            entity.HasOne(d => d.Usuario1).WithMany(p => p.ConversacioneUsuario1s)
                .HasForeignKey(d => d.Usuario1Id)
                .HasConstraintName("conversaciones_ibfk_1");

            entity.HasOne(d => d.Usuario2).WithMany(p => p.ConversacioneUsuario2s)
                .HasForeignKey(d => d.Usuario2Id)
                .HasConstraintName("conversaciones_ibfk_2");
        });

        modelBuilder.Entity<Curso>(entity =>
        {
            entity.HasKey(e => e.CodCurso).HasName("PRIMARY");

            entity.ToTable("cursos");

            entity.HasIndex(e => e.CodAcademia, "academia_fk");

            entity.Property(e => e.CodCurso).HasColumnName("cod_curso");
            entity.Property(e => e.CodAcademia).HasColumnName("cod_academia");
            entity.Property(e => e.Descripcion)
                .HasColumnType("text")
                .HasColumnName("descripcion");
            entity.Property(e => e.FechaFinCurso)
                .HasColumnType("datetime")
                .HasColumnName("fecha_fin_curso");
            entity.Property(e => e.FechaInicioCurso)
                .HasColumnType("datetime")
                .HasColumnName("fecha_inicio_curso");
            entity.Property(e => e.NombreCurso)
                .HasMaxLength(50)
                .HasColumnName("nombre_curso");
            entity.Property(e => e.Precio).HasColumnName("precio");
            entity.Property(e => e.Tipo)
                .HasColumnType("enum('Académico','Otros')")
                .HasColumnName("tipo");
            entity.Property(e => e.Valoracion).HasColumnName("valoracion");

            entity.HasOne(d => d.CodAcademiaNavigation).WithMany(p => p.Cursos)
                .HasForeignKey(d => d.CodAcademia)
                .HasConstraintName("academia_fk");
        });

        modelBuilder.Entity<Favorito>(entity =>
        {
            entity
                .HasNoKey()
                .ToTable("favoritos");

            entity.HasIndex(e => e.CodCurso, "cursoFavoritos_fk");

            entity.HasIndex(e => e.Usuario, "usuarioFavoritos_fk");

            entity.Property(e => e.CodCurso).HasColumnName("cod_curso");
            entity.Property(e => e.Usuario)
                .HasMaxLength(50)
                .HasColumnName("usuario");

            entity.HasOne(d => d.CodCursoNavigation).WithMany()
                .HasForeignKey(d => d.CodCurso)
                .HasConstraintName("cursoFavoritos_fk");

            entity.HasOne(d => d.UsuarioNavigation).WithMany()
                .HasForeignKey(d => d.Usuario)
                .HasConstraintName("usuarioFavoritos_fk");
        });

        modelBuilder.Entity<Inscripcione>(entity =>
        {
            entity
                .HasNoKey()
                .ToTable("inscripciones");

            entity.HasIndex(e => e.CodCurso, "curso_fk");

            entity.HasIndex(e => e.Usuario, "usuario_fk");

            entity.Property(e => e.CodCurso).HasColumnName("cod_curso");
            entity.Property(e => e.FechaMiFinCurso)
                .HasColumnType("datetime")
                .HasColumnName("fecha_MiFin_curso");
            entity.Property(e => e.FechaMiInicioCurso)
                .HasColumnType("datetime")
                .HasColumnName("fecha_MiInicio_curso");
            entity.Property(e => e.Usuario)
                .HasMaxLength(50)
                .HasColumnName("usuario");

            entity.HasOne(d => d.CodCursoNavigation).WithMany()
                .HasForeignKey(d => d.CodCurso)
                .HasConstraintName("curso_fk");

            entity.HasOne(d => d.UsuarioNavigation).WithMany()
                .HasForeignKey(d => d.Usuario)
                .HasConstraintName("usuario_fk");
        });

        modelBuilder.Entity<Mensaje>(entity =>
        {
            entity
                .HasNoKey()
                .ToTable("mensaje");

            entity.HasIndex(e => e.CodConversacion, "conversacion_fk");

            entity.HasIndex(e => e.SenderId, "senderId");

            entity.Property(e => e.CodConversacion).HasColumnName("cod_conversacion");
            entity.Property(e => e.CodMensaje).HasColumnName("cod_mensaje");
            entity.Property(e => e.Contenido)
                .HasMaxLength(500)
                .HasColumnName("contenido");
            entity.Property(e => e.SenderId)
                .HasMaxLength(50)
                .HasColumnName("senderId");
            entity.Property(e => e.Timestamp)
                .HasColumnType("datetime")
                .HasColumnName("timestamp");

            entity.HasOne(d => d.CodConversacionNavigation).WithMany()
                .HasForeignKey(d => d.CodConversacion)
                .HasConstraintName("conversacion_fk");

            entity.HasOne(d => d.Sender).WithMany()
                .HasForeignKey(d => d.SenderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("mensaje_ibfk_1");
        });

        modelBuilder.Entity<Usuario>(entity =>
        {
            entity.HasKey(e => e.Usuario1).HasName("PRIMARY");

            entity.ToTable("usuarios");

            entity.HasIndex(e => e.Email, "email").IsUnique();

            entity.Property(e => e.Usuario1)
                .HasMaxLength(50)
                .HasColumnName("usuario");
            entity.Property(e => e.Apellidos)
                .HasMaxLength(40)
                .HasColumnName("apellidos");
            entity.Property(e => e.Contrasena)
                .HasColumnType("text")
                .HasColumnName("contrasena");
            entity.Property(e => e.Edad).HasColumnName("edad");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .HasColumnName("email");
            entity.Property(e => e.ImgPerfil)
                .HasMaxLength(100)
                .HasColumnName("img_perfil");
            entity.Property(e => e.Nombre)
                .HasMaxLength(20)
                .HasColumnName("nombre");
            entity.Property(e => e.Telefono).HasColumnName("telefono");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
