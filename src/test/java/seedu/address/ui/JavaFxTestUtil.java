package seedu.address.ui;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * Utility helpers for running assertions on the JavaFX Application Thread.
 * Needed because many UI components require construction and interaction on that thread.
 */
final class JavaFxTestUtil {

    private static final long FX_TIMEOUT_SECONDS = 5;
    private static final AtomicBoolean INITIALISED = new AtomicBoolean(false);

    private JavaFxTestUtil() {
    }

    /**
     * Ensures the JavaFX toolkit is started before interacting with JavaFX components.
     */
    static void initFxToolkit() {
        if (INITIALISED.get()) {
            return;
        }
        synchronized (INITIALISED) {
            if (INITIALISED.get()) {
                return;
            }
            try {
                SwingUtilities.invokeAndWait(JFXPanel::new);
                CompletableFuture<Void> ready = new CompletableFuture<>();
                Platform.runLater(() -> {
                    Platform.setImplicitExit(false);
                    ready.complete(null);
                });
                ready.get(FX_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while starting JavaFX toolkit", e);
            } catch (ExecutionException | TimeoutException e) {
                throw new IllegalStateException("Failed to start JavaFX toolkit", e);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to start JavaFX toolkit", e);
            } finally {
                INITIALISED.set(true);
            }
        }
    }

    /**
     * Runs the given {@code runnable} on the JavaFX Application Thread and waits for completion.
     */
    static void runOnFxThreadAndWait(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
            return;
        }
        CompletableFuture<Void> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });
        waitForFuture(future);
    }

    /**
     * Executes the supplied {@code callable} on the JavaFX Application Thread and returns its result.
     */
    static <T> T callOnFxThread(Callable<T> callable) {
        if (Platform.isFxApplicationThread()) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw rethrow(e);
            }
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                future.complete(callable.call());
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });
        return waitForFuture(future);
    }

    private static <T> T waitForFuture(CompletableFuture<T> future) {
        try {
            return future.get(FX_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for JavaFX task", e);
        } catch (ExecutionException e) {
            throw rethrow(e.getCause());
        } catch (TimeoutException e) {
            throw new IllegalStateException("Timed out waiting for JavaFX task", e);
        }
    }

    private static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        return new RuntimeException(throwable);
    }
}
