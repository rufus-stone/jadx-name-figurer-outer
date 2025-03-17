package jadx.plugins.tfoc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jadx.api.plugins.pass.JadxPassInfo;
import jadx.api.plugins.pass.impl.OrderedJadxPassInfo;
import jadx.api.plugins.pass.types.JadxDecompilePass;
import jadx.core.deobf.NameMapper;
import jadx.core.dex.nodes.BlockNode;
import jadx.core.dex.nodes.InsnNode;
import jadx.core.dex.nodes.ClassNode;
import jadx.core.dex.nodes.MethodNode;
import jadx.core.dex.nodes.RootNode;
import jadx.core.dex.info.FieldInfo;
import jadx.core.dex.info.MethodInfo;
import jadx.core.dex.instructions.IndexInsnNode;
import jadx.core.dex.instructions.InsnType;
import jadx.core.dex.instructions.InvokeNode;
import jadx.core.dex.instructions.args.InsnArg;
import jadx.core.dex.instructions.args.InsnWrapArg;
import jadx.core.dex.instructions.args.RegisterArg;

public class FigureOutNamesPass implements JadxDecompilePass {

	private static final Logger LOG = LoggerFactory.getLogger(FigureOutNamesPass.class);

	// TODO: Add more methods, including optString() and friends
	private static final List<String> jsonMethods = new ArrayList<>(List.of(
			// JSON get* methods
			"org.json.JSONObject.getString(Ljava/lang/String;)Ljava/lang/String;",
			"org.json.JSONObject.getInt(Ljava/lang/String;)I",
			"org.json.JSONObject.getLong(Ljava/lang/String;)J",
			"org.json.JSONObject.getBoolean(Ljava/lang/String;)Z",
			"org.json.JSONObject.getDouble(Ljava/lang/String;)D",

			// JSON opt* methods
			"org.json.JSONObject.optString(Ljava/lang/String;)Ljava/lang/String;",
			"org.json.JSONObject.optString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
			"org.json.JSONObject.optInt(Ljava/lang/String;)I",
			"org.json.JSONObject.optInt(Ljava/lang/String;I)I",
			"org.json.JSONObject.optLong(Ljava/lang/String;)J",
			"org.json.JSONObject.optLong(Ljava/lang/String;J)J",
			"org.json.JSONObject.optDouble(Ljava/lang/String;)D",
			"org.json.JSONObject.optDouble(Ljava/lang/String;D)D",
			"org.json.JSONObject.optBoolean(Ljava/lang/String;)Z",
			"org.json.JSONObject.optBoolean(Ljava/lang/String;Z)Z",

			// Bundle methods
			// TODO: Add tests for these
			"android.os.Bundle.getString(Ljava/lang/String;)Ljava/lang/String;",
			"android.os.Bundle.getInt(Ljava/lang/String;I)I",
			"android.os.Bundle.getBoolean(Ljava/lang/String;Z)Z"));

	public static String formatNewName(String rawName) {
		LOG.debug("Raw name: {}", rawName);

		// Expect inputs to be of the format ("name") with surrounding parentheses and
		// double quotes, so need minimum of 5 chars
		if (rawName.length() <= 4) {
			return null;
		}

		String newName = rawName.substring(0, rawName.length() - 2).substring(2);
		newName = newName.replace('.', '_');
		LOG.debug("New name: {}", newName);

		if (NameMapper.isValidIdentifier(newName)) {
			return newName;
		} else {
			return null;
		}
	}

	public static void handleInvoke(InsnNode insn) {
		LOG.debug("Handling INVOKE");

		InvokeNode invokeNode = ((InvokeNode) insn);

		LOG.debug("as InvokeNode: {}", invokeNode);
		LOG.debug("getInstanceArg: {}", invokeNode.getInstanceArg());
		LOG.debug("getResult: {}", invokeNode.getResult());

		RegisterArg result = invokeNode.getResult();
		MethodInfo invokedMethod = invokeNode.getCallMth();

		LOG.debug("invokedMethod: {}", invokedMethod.getRawFullId());

		if (jsonMethods.contains(invokedMethod.getRawFullId())) {
			LOG.debug("It's a JSON method!");
			InsnArg invokeArg = invokeNode.getArg(1);

			LOG.debug("Invoked with arg: {} ({})", invokeArg, invokeArg.getType());

			String newName = formatNewName(invokeArg.toString());

			// Rename the local variable
			if (newName != null) {
				result.setName(newName);
			}
		}

	}

	public static void handleIput(InsnNode insn) {
		LOG.debug("Handling IPUT");

		IndexInsnNode idxNode = ((IndexInsnNode) insn);

		LOG.debug("as IndexInsnNode: {}", idxNode);
		LOG.debug("as getIndex: {}", idxNode.getIndex());

		if (!(idxNode.getIndex() instanceof FieldInfo)) {
			return;
		}

		FieldInfo destFieldInfo = ((FieldInfo) idxNode.getIndex());

		LOG.debug("destFieldInfo: {}", destFieldInfo);
		LOG.debug("destFieldInfo.getFullId(): {}", destFieldInfo.getFullId());
		LOG.debug("destFieldInfo.getDeclClass(): {}", destFieldInfo.getDeclClass());

		if (!insn.containsWrappedInsn()) {
			return;
		}

		if (insn.getArgsCount() != 2) {
			return;
		}

		InsnArg arg0 = insn.getArg(0);
		InsnArg arg1 = insn.getArg(1);

		LOG.debug("arg0 ({}): {}", arg0.getType(), arg0);
		LOG.debug("arg1 ({}): {}", arg1.getType(), arg1);

		if (!(arg0 instanceof InsnWrapArg)) {
			return;
		}

		if (!arg0.isInsnWrap()) {
			return;
		}

		InsnNode wrappedInsn = arg0.unwrap();

		if (wrappedInsn.getType() != InsnType.INVOKE) {
			return;
		}

		InvokeNode invokeNode = ((InvokeNode) wrappedInsn);

		// TODO: Make this more sensible, as some methods we want to look for take
		// different numbers of args
		if (invokeNode.getArgsCount() < 2) { // != 2) {
			return;
		}
		MethodInfo invokedMethod = invokeNode.getCallMth();

		LOG.debug("arg0: invokeNode: {}", invokeNode);
		LOG.debug("arg0: invokedMethod: {}", invokedMethod.getRawFullId());

		if (jsonMethods.contains(invokedMethod.getRawFullId())) {
			LOG.debug("It's a JSON method!");
			InsnArg invokeArg = invokeNode.getArg(1);

			LOG.debug("Invoked with arg: {} ({})", invokeArg, invokeArg.getType());

			String newName = formatNewName(invokeArg.toString());

			// Rename the field
			if (newName != null) {
				destFieldInfo.setAlias(newName);
			}

		}
	}

	@Override
	public JadxPassInfo getInfo() {
		return new OrderedJadxPassInfo(
				"WrangleJSON",
				"Wrangle JSON names");
		// .after("FinishTypeInference");
		// .before("RegionMakerVisitor");
	}

	@Override
	public void init(RootNode root) {
	}

	@Override
	public boolean visit(ClassNode cls) {
		return true;
	}

	@Override
	public void visit(MethodNode mth) {
		LOG.debug("MethodNode: " + mth.toString());

		List<BlockNode> basicBlocks = mth.getBasicBlocks();

		if (basicBlocks == null) {
			return;
		}

		for (BlockNode basicBlock : basicBlocks) {
			List<InsnNode> insns = basicBlock.getInstructions();

			for (InsnNode insn : insns) {
				LOG.debug("\n\n+++++++++++++++++++++++++++++++++++\n\n");
				LOG.debug("basicBlock insn: {}", insn);
				LOG.debug("basicBlock insn type: {}", insn.getType());

				// Handle IPUT or INVOKE
				switch (insn.getType()) {
					case INVOKE:
						handleInvoke(insn);
						break;

					case IPUT:
						handleIput(insn);
						break;

					default:
						LOG.debug("Skipping as not INVOKE or IPUT");
						continue;
				}
			}
		}
	}
}
