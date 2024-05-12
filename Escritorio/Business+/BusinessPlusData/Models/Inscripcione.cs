using System;
using System.Collections.Generic;
using System.ComponentModel;

namespace BusinessPlusData.Models;

public partial class Inscripcione
{
    public int CodCurso { get; set; }

    public string Usuario { get; set; } = null!;

    [Browsable(false)]
    public virtual Curso CodCursoNavigation { get; set; } = null!;

    [Browsable(false)]
    public virtual Usuario UsuarioNavigation { get; set; } = null!;
}
