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
    /// Lógica de interacción para ConversacionesUserControl.xaml
    /// </summary>
    public partial class ConversacionesUserControl : UserControl
    {
        public ConversacionesUserControl()
        {
            InitializeComponent();
            DataContext = new MainViewModel();
        }

        private void AbrirChat(object sender, MouseButtonEventArgs e)
        {

        }
    }
}
