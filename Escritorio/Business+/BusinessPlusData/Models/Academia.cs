using System;
using System.Collections.Generic;

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

    public virtual ICollection<Curso> Cursos { get; set; } = new List<Curso>();
}
