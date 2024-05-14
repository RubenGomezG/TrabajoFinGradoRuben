using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para TarjetaConversacion.xaml
    /// </summary>
    public partial class TarjetaConversacion : UserControl
    {
        public static DependencyProperty NombreProperty = DependencyProperty.Register("Nombre", typeof(string), typeof(TarjetaConversacion), new PropertyMetadata(string.Empty));

        public static DependencyProperty NombreAcademiaProperty = DependencyProperty.Register("NombreAcademia", typeof(string), typeof(TarjetaConversacion), new PropertyMetadata(string.Empty));


        public static DependencyProperty MensajeProperty = DependencyProperty.Register("Mensaje", typeof(string), typeof(TarjetaConversacion), new PropertyMetadata(string.Empty));


        public static DependencyProperty FotoProperty = DependencyProperty.Register("Foto", typeof(string), typeof(TarjetaConversacion), new PropertyMetadata(string.Empty));

        public string Nombre
        {
            get { return (string)GetValue(NombreProperty); }
            set { SetValue(NombreProperty, value); }
        }

        public string NombreAcademia
        {
            get { return (string)GetValue(NombreAcademiaProperty); }
            set { SetValue(NombreAcademiaProperty, value); }
        }

        public string Mensaje
        {
            get { return (string)GetValue(MensajeProperty); }
            set { SetValue(MensajeProperty, value); }
        }

        public string Foto
        {
            get { return (string)GetValue(FotoProperty); }
            set { SetValue(FotoProperty, value); }
        }
        public TarjetaConversacion()
        {
            InitializeComponent();
        }
    }
}
