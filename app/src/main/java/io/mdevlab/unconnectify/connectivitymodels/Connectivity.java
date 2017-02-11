package io.mdevlab.unconnectify.connectivitymodels;

/**
 * Created by bachiri on 2/10/17.
 */


/**
 * This is the parent class for all Connectivity models it contains two abstract methods enable/disable
 * all connectivity should extend it
 *
 */
public abstract class Connectivity {

    /**
     * this class will be the main class for enabling any connectivity
     */
    protected abstract void enable();

    /**
     * this class will be the main class for disabling  any connectivity
     */
    protected abstract void disable();
}
