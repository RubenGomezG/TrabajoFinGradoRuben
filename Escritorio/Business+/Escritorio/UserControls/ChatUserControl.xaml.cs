﻿using System;
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
using BusinessPlusData.Models;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para ChatUserControl.xaml
    /// </summary>
    public partial class ChatUserControl : UserControl
    {
        public ChatUserControl()
        {
            InitializeComponent();
            //DataContext = new MainViewModel();
        }

        private void EnviarMensaje(object sender, RoutedEventArgs e)
        {

        }
    }
}
