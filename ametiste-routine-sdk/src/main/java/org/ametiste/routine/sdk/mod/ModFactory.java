package org.ametiste.routine.sdk.mod;

/**
 *
 * @since
 */
public interface ModFactory {

    Mod createMod(TaskGateway taskGateway, DataGateway dataGateway);

}
