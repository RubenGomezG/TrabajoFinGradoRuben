using System;
using System.Collections.Generic;

namespace BusinessPlusData.Models;

public partial class Mensaje
{
    public int CodMensaje { get; set; }

    public int CodConversacion { get; set; }

    public string SenderId { get; set; } = null!;

    public string Contenido { get; set; } = null!;

    public DateTime Timestamp { get; set; }

    public virtual Conversacione CodConversacionNavigation { get; set; } = null!;

    public virtual Usuario Sender { get; set; } = null!;
}
