package cn.eclipse.code.review;

import java.io.File;

import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.scienjus.smartqq.QNUploader;

/**
 * The activator class controls the plug-in life cycle
 */
public class CRPlugin extends AbstractUIPlugin {
    
    // The plug-in ID
	public static final String PLUGIN_ID = "plugin-review.core"; //$NON-NLS-1$
    
    // The shared instance
    private static CRPlugin plugin;
    
    /**
     * The constructor
     */
    public CRPlugin() {
    }
    
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static CRPlugin getDefault() {
        if (plugin == null) {
			plugin = new CRPlugin();
        }
        return plugin;
    }
    
    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    public static ImageDescriptor getSharedImage(String name) {
        return PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(name);
    }
    
    // -------->
    private QNUploader uploader;
    
    public IProxyService proxyService;
    
    public IProxyService getProxyService() {
        if (proxyService == null) {
            ServiceReference<IProxyService> ref = getBundle().getBundleContext()
                    .getServiceReference(IProxyService.class);
            proxyService = getBundle().getBundleContext().getService(ref);
        }
        
        return proxyService;
    }
    
    public QNUploader getUploader() {
        if (uploader == null) {
            uploader = new QNUploader();
        }
        return uploader;
    }
    
    public File getStateDir() {
        return getStateLocation().makeAbsolute().toFile();
    }
    
    public void log(String msg, Throwable e) {
        if (e == null) {
            IStatus info = new Status(IStatus.INFO, CRPlugin.PLUGIN_ID, msg);
            getLog().log(info);
        }
        else {
            IStatus info = new Status(IStatus.ERROR, CRPlugin.PLUGIN_ID, msg,
                    e);
            getLog().log(info);
        }
    }
    
    public void log(String msg) {
        log(msg, null);
    }
    
	public void warn(String msg) {
        IStatus info = new Status(IStatus.WARNING, CRPlugin.PLUGIN_ID, msg);
        getLog().log(info);
    }
    
    public static void runOnUI(Runnable runnable) {
        Display display = Display.getDefault();
        if (display != null) {
            display.asyncExec(runnable);
        }
    }
    
    public boolean enable = true;
    
    public static void setEnable(boolean enable) {
        getDefault().enable = enable;
    }
    
}
