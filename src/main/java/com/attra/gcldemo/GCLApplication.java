package com.attra.gcldemo;

import com.attra.gcldemo.exceptions.GoCardlessApiExceptionMapper;
import com.attra.gcldemo.exceptions.InvalidSignatureExceptionMapper;
import com.attra.gcldemo.resources.RedirectFlowResource;
import com.attra.gcldemo.resources.WebhookResource;

import com.gocardless.GoCardlessClient;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.server.session.SessionHandler;

public class GCLApplication extends Application<GCLConfiguration> {
    @Override
    protected void bootstrapLogging() {
    }

    public static void main( String[] args ) throws Exception {
        // TODO: A bit of hard-coding convenience during development
        // NOTE: Ideally these params should be passed to dropWizard Applications at Runtime from the Command Line
        new GCLApplication().run( "server", "config.dev.yml" );
    }

    @Override
    public void initialize( Bootstrap<GCLConfiguration> bootstrap ) {

        bootstrap.addBundle( new ViewBundle<>() );
        bootstrap.addBundle( new AssetsBundle() );

        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false )
            )
        );
    }

    @Override
    public void run(GCLConfiguration configuration, Environment environment) {

        GoCardlessClient goCardless = configuration.getGoCardless().buildClient();
        System.out.println( "GoCardlessClient Value is " + goCardless );

        environment.jersey().register( new RedirectFlowResource( goCardless ) );
        environment.jersey().register( new WebhookResource( configuration.getGoCardless().getWebhookSecret() ) );
        environment.jersey().register( new GoCardlessApiExceptionMapper() );
        environment.jersey().register( new InvalidSignatureExceptionMapper() );

        environment.servlets().setSessionHandler( new SessionHandler() );
    }
}
