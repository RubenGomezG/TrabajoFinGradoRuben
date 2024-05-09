using System;
using System.Collections.Generic;

namespace BusinessPlusData.Models;

public partial class Curso
{
    public int CodCurso { get; set; }

    public string NombreCurso { get; set; } = null!;

    public DateTime FechaInicioCurso { get; set; }

    public DateTime FechaFinCurso { get; set; }

    public double Precio { get; set; }

    public double Valoracion { get; set; }

    public string? Descripcion { get; set; }

    public string Tipo { get; set; } = null!;

    public int CodAcademia { get; set; }

    public virtual Academia CodAcademiaNavigation { get; set; } = null!;
}
