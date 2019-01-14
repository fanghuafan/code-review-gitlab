/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.eclipse.code.review.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.eclipse.code.review.CRPlugin;

public class HotKeyPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
	public static final String KEY_ADD = CRPlugin.PLUGIN_ID + ".key.send"; //$NON-NLS-1$
	public static final String KEY_VIEW = CRPlugin.PLUGIN_ID + ".key.list.view"; //$NON-NLS-1$
            
    public HotKeyPreferencePage() {
        super(GRID);
		setPreferenceStore(CRPlugin.getDefault().getPreferenceStore());
		setDescription("Please setting hot key!");
    }
    
    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
		HotKeyFieldEditor send = new HotKeyFieldEditor(KEY_ADD,
				"ADD Code Review", getFieldEditorParent());
        addField(send);
		addField(new HotKeyFieldEditor(KEY_VIEW,
				"Open Code Review window",
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}