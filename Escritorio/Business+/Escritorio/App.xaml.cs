using BusinessPlusData.Models;
using BusinessPlusData.ViewModels;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using System.Configuration;
using System.Data;
using System.IO;
using System.Windows;

namespace Escritorio
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        private readonly IServiceProvider _serviceProvider;

        public static Academia? LoggedAcademia { get; set; }
        public static string rutaDebug = "";
        public static string rutaBin = "";
        public static string rutaRaiz = "";
        public App()
        {
            ServiceCollection services = new ServiceCollection();
            ConfigureServices(services);
            _serviceProvider = services.BuildServiceProvider();
            rutaDebug = Directory.GetCurrentDirectory();
            rutaBin = Directory.GetParent(rutaDebug).Parent.FullName;
            rutaRaiz = Directory.GetParent(rutaBin).Parent.FullName;
        }

        private void ConfigureServices(ServiceCollection services)
        {
            services.AddDbContext<Bu5x9ctsBusinessplusContext>(options =>
            {
                var connectionString = "server=lhcp3379.webapps.net;database=bu5x9cts_businessplus;uid=bu5x9cts_rubengomez;pwd=Clash1ng!";
                options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
            });

            // Registra tus ViewModels y servicios
            services.AddTransient<MainViewModel>();
            services.AddTransient<ConversacionesViewModel>();
            services.AddTransient<ChatViewModel>();
            services.AddTransient<UsuarioViewModel>();
        }

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            var mainWindow = _serviceProvider.GetService<MainWindow>();
            mainWindow?.Show();
        }
    }

}
