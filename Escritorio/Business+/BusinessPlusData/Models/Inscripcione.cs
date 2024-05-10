﻿using System;
using System.Collections.Generic;

namespace BusinessPlusData.Models;

public partial class Inscripcione
{
    public int CodCurso { get; set; }

    public string Usuario { get; set; } = null!;

    public DateTime FechaMiInicioCurso { get; set; }

    public DateTime FechaMiFinCurso { get; set; }

    public virtual Curso CodCursoNavigation { get; set; } = null!;

    public virtual Usuario UsuarioNavigation { get; set; } = null!;
}