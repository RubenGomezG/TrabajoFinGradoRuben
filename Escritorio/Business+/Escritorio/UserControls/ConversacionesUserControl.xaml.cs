using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
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


        private readonly ConversacionRepository _repository;
        private readonly Bu5x9ctsBusinessplusContext _context;

        public ConversacionesUserControl()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _repository = new ConversacionRepository(_context);
            this.DataContext = _repository.GetAllConversaciones();
        }

        private void AbrirChat(object sender, MouseButtonEventArgs e)
        {
            var panel = (StackPanel)sender;
            var codConversacion = (int)panel.Tag;
            var chat = new Chat();
            chat.CargarMensajes(codConversacion);
            chat.Show();
        }

    }
}
