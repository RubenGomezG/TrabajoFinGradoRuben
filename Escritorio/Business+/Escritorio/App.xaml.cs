using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using System.Configuration;
using System.Data;
using System.Windows;

namespace Escritorio
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        private readonly IServiceProvider _serviceProvider;

        public App()
        {
            ServiceCollection services = new ServiceCollection();
            ConfigureServices(services);
            _serviceProvider = services.BuildServiceProvider();
        }

        private void ConfigureServices(ServiceCollection services)
        {
            // Asume que "MiDbContext" es tu clase DbContext
            services.AddDbContext<Bu5x9ctsBusinessplusContext>(options =>
            {
                var connectionString = "server=lhcp3379.webapps.net;database=bu5x9cts_businessplus;uid=bu5x9cts_rubengomez;pwd=Clash1ng!";
                options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
            });

            // Registra tus ViewModels y servicios
            services.AddTransient<MainViewModel>();
            // ... otros servicios y ViewModels
        }

        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            var mainWindow = _serviceProvider.GetService<MainWindow>();
            mainWindow?.Show();
        }
    }

}
