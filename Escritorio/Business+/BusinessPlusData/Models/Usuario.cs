using System;
using System.Collections.Generic;
using System.ComponentModel;

namespace BusinessPlusData.Models;

public partial class Usuario
{
    public string Usuario1 { get; set; } = null!;

    public string Contrasena { get; set; } = null!;

    public string Email { get; set; } = null!;

    public string Nombre { get; set; } = null!;

    public string Apellidos { get; set; } = null!;

    public int Telefono { get; set; }

    public string? ImgPerfil { get; set; }

    public int? Edad { get; set; }

    [Browsable(false)]
    public virtual ICollection<Conversacione> ConversacioneUsuario2s { get; set; } = new List<Conversacione>();

    [Browsable(false)]
    public virtual ICollection<Mensaje> Mensajes { get; set; } = new List<Mensaje>();

    [Browsable(false)]
    public virtual ICollection<Inscripcione> Inscripciones { get; set; } = new List<Inscripcione>();
}
