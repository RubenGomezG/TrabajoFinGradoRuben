﻿#pragma checksum "..\..\..\..\UserControls\UsuariosUserControl.xaml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "371BE23FD785F30A91A29A38D9882D474B3F1EDF"
//------------------------------------------------------------------------------
// <auto-generated>
//     Este código fue generado por una herramienta.
//     Versión de runtime:4.0.30319.42000
//
//     Los cambios en este archivo podrían causar un comportamiento incorrecto y se perderán si
//     se vuelve a generar el código.
// </auto-generated>
//------------------------------------------------------------------------------

using Escritorio.UserControls;
using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Controls.Ribbon;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Media.TextFormatting;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Shell;


namespace Escritorio.UserControls {
    
    
    /// <summary>
    /// UsuariosUserControl
    /// </summary>
    public partial class UsuariosUserControl : System.Windows.Controls.UserControl, System.Windows.Markup.IComponentConnector {
        
        
        #line 46 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtBuscarUsuarios;
        
        #line default
        #line hidden
        
        
        #line 47 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtUsername;
        
        #line default
        #line hidden
        
        
        #line 48 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtNombreUsuario;
        
        #line default
        #line hidden
        
        
        #line 49 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtApellidos;
        
        #line default
        #line hidden
        
        
        #line 50 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtEmail;
        
        #line default
        #line hidden
        
        
        #line 51 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtTelefono;
        
        #line default
        #line hidden
        
        
        #line 52 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox txtEdad;
        
        #line default
        #line hidden
        
        
        #line 55 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Image imgCurso;
        
        #line default
        #line hidden
        
        
        #line 67 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.ListBox listaCursos;
        
        #line default
        #line hidden
        
        
        #line 73 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.DataGrid dataGridUsuarios;
        
        #line default
        #line hidden
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "8.0.4.0")]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Uri resourceLocater = new System.Uri("/Escritorio;component/usercontrols/usuariosusercontrol.xaml", System.UriKind.Relative);
            
            #line 1 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
            System.Windows.Application.LoadComponent(this, resourceLocater);
            
            #line default
            #line hidden
        }
        
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "8.0.4.0")]
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Never)]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Design", "CA1033:InterfaceMethodsShouldBeCallableByChildTypes")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Maintainability", "CA1502:AvoidExcessiveComplexity")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1800:DoNotCastUnnecessarily")]
        void System.Windows.Markup.IComponentConnector.Connect(int connectionId, object target) {
            switch (connectionId)
            {
            case 1:
            this.txtBuscarUsuarios = ((System.Windows.Controls.TextBox)(target));
            
            #line 46 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
            this.txtBuscarUsuarios.TextChanged += new System.Windows.Controls.TextChangedEventHandler(this.BuscarUsuarios);
            
            #line default
            #line hidden
            return;
            case 2:
            this.txtUsername = ((System.Windows.Controls.TextBox)(target));
            return;
            case 3:
            this.txtNombreUsuario = ((System.Windows.Controls.TextBox)(target));
            return;
            case 4:
            this.txtApellidos = ((System.Windows.Controls.TextBox)(target));
            return;
            case 5:
            this.txtEmail = ((System.Windows.Controls.TextBox)(target));
            return;
            case 6:
            this.txtTelefono = ((System.Windows.Controls.TextBox)(target));
            return;
            case 7:
            this.txtEdad = ((System.Windows.Controls.TextBox)(target));
            return;
            case 8:
            this.imgCurso = ((System.Windows.Controls.Image)(target));
            return;
            case 9:
            this.listaCursos = ((System.Windows.Controls.ListBox)(target));
            return;
            case 10:
            this.dataGridUsuarios = ((System.Windows.Controls.DataGrid)(target));
            
            #line 78 "..\..\..\..\UserControls\UsuariosUserControl.xaml"
            this.dataGridUsuarios.SelectionChanged += new System.Windows.Controls.SelectionChangedEventHandler(this.ElegirColumna);
            
            #line default
            #line hidden
            return;
            }
            this._contentLoaded = true;
        }
    }
}

