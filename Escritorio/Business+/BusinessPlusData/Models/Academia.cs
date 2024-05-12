using System;
using System.Collections.Generic;
using System.ComponentModel;

namespace BusinessPlusData.Models;

public partial class Academia
{
    public int CodAcademia { get; set; }

    public string Usuario { get; set; } = null!;

    public string Contrasena { get; set; } = null!;

    public string Email { get; set; } = null!;

    public string Nombre { get; set; } = null!;

    public int Telefono { get; set; }

    public string Direccion { get; set; } = null!;

    public double Latitud { get; set; }

    public double Longitud { get; set; }

    public string? ImgPerfil { get; set; }

    [Browsable(false)]
    public virtual ICollection<Curso> Cursos { get; set; } = new List<Curso>();

    [Browsable(false)]
    public virtual ICollection<Conversacione> ConversacioneUsuario1s { get; set; } = new List<Conversacione>();

    [Browsable(false)]
    public virtual ICollection<Mensaje> Mensajes { get; set; } = new List<Mensaje>();
}
