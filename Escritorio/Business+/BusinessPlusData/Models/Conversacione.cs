using System;
using System.Collections.Generic;

namespace BusinessPlusData.Models;

public partial class Conversacione
{
    public int CodConversacion { get; set; }

    public string Usuario1Id { get; set; } = null!;

    public string Usuario2Id { get; set; } = null!;

    public virtual Usuario Usuario1 { get; set; } = null!;

    public virtual Usuario Usuario2 { get; set; } = null!;
}
