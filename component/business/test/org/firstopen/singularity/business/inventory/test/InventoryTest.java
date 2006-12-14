package org.firstopen.singularity.business.inventory.test;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.firstopen.singularity.business.TagIdMap;
import org.firstopen.singularity.business.inventory.InventorySLSBHome;
import org.firstopen.singularity.business.inventory.InventorySLSBRemote;
import org.firstopen.singularity.util.StringUtil;

public class InventoryTest extends TestCase {
	protected InventorySLSBRemote iSLSB = null;

	public InventoryTest(String name) throws Exception {
		super(name);
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		env
				.put(Context.URL_PKG_PREFIXES,
						"org.jboss.naming:org.jnp.interfaces");
		InitialContext jndiContext = new InitialContext(env);
		InventorySLSBHome iHome = (InventorySLSBHome) jndiContext
				.lookup("ejb/inventory/InventorySLSB");
		iSLSB = iHome.create();
	}

	protected void setUp() throws Exception {
	}

	public void testInventoryRequestMethods() throws Exception {
		iSLSB.updateInventory("DEN", "HON92FrSus", new Double(20.0), null);
	}

	public void testTagIdMapMethods() throws Exception {
		int[] epcClass0Tag = new int[] { 0x11, 0x17, 0x01, 0x03, 0x08, 0x05,
				0x07, 0xa8, 0x02, 0x00, 0x10, 0x00, 0x3e, 0x3a, 0x5e };
		String tagId = new String(StringUtil.intArrayToString(epcClass0Tag,
				false));

		HashMap newHash = new HashMap();
		newHash.put("tagId", tagId);
		newHash.put("binId", "Bin_3");
		newHash.put("itemId", "HON92FrSus");
		newHash.put("quantity", new Double(2));
		newHash.put("location", "DEN");
		iSLSB.createOrUpdateTagIdMap(newHash);
	}

	public void testTagIdMapCache() throws Exception {
		int[] epcClass0Tag = new int[] { 0x11, 0x17, 0x01, 0x03, 0x08, 0x05,
				0x07, 0xa8, 0x02, 0x00, 0x10, 0x00, 0x3e, 0x3a, 0x5e };
		String tagId = new String(StringUtil.intArrayToString(epcClass0Tag,
				false));
		TagIdMap tagIdMap = null;
		for (int x = 0; x < 10; x++) {
			tagIdMap = iSLSB.getTagIdMap(tagId);
			System.out.println("binId = " + tagIdMap.getBinId());
		}

		// iSLSB.deleteTagIdMap(tagId);
	}

	public static Test suite() {
		return new TestSuite(InventoryTest.class);
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
