using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace BusinessPlusData.Models;

public partial class Mensaje
{
    [Key]
    public int CodMensaje { get; set; }

    public int CodConversacion { get; set; }

    public string SenderUsername { get; set; } = null!;
    public int SenderCodAcademia { get; set; }

    public string Contenido { get; set; } = null!;

    public DateTime Timestamp { get; set; }

    [Browsable(false)]
    public virtual Conversacione CodConversacionNavigation { get; set; } = null!;

    [Browsable(false)]
    public virtual Usuario Sender { get; set; } = null!;

    [Browsable(false)]
    public virtual Academia SenderAcademia { get; set; } = null!;
}
