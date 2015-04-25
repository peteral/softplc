package de.peteral.softplc.model;

/**
 * Classes implementing this interface can register to be informed about the
 * life cycle of a {@link Program} execution.
 *
 * @author peteral
 */
public interface ProgramCycleObserver
{
    /**
     * Invoked after a program cycle was finished.
     */
    void afterCycleEnd();

    /**
     * Invoked in case of error just before the exception would be thrown.
     *
     * @param e
     *        exception about to be thrown
     * @return false - exception has been handled, do not throw it
     */
    boolean onError(String context, Throwable e);
}
