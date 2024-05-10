using System;
using System.Collections.Generic;

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

    public virtual ICollection<Conversacione> ConversacioneUsuario1s { get; set; } = new List<Conversacione>();

    public virtual ICollection<Conversacione> ConversacioneUsuario2s { get; set; } = new List<Conversacione>();
}
