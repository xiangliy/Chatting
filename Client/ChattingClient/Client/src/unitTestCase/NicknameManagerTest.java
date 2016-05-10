package unitTestCase;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.yexl.client.NicknameManager;

public class NicknameManagerTest {

	@Test
	public void testGetMyNickName() {
		NicknameManager.getInstance().signin("yexl1");
		NicknameManager.getInstance().signin("yexl2");
		Set<String> s = NicknameManager.getInstance().getNicknameList();
		
		Set<String> target = new HashSet<String>();
		target.add("yexl1");
		target.add("yexl2");
		assertEquals(s, target);
		
		NicknameManager.getInstance().signout("yexl1");
		NicknameManager.getInstance().signout("yexl2");
	}

	@Test
	public void testSignin() {
		assertEquals(NicknameManager.getInstance().signin("yexl"), true);
		assertEquals(NicknameManager.getInstance().signout("yexl"), true);
	}

}
