package student_pack;

import java.util.ArrayList;

public class PlaylistNodePrimaryIndex extends PlaylistNode {
	private ArrayList<Integer> audioIds;
	private ArrayList<PlaylistNode> children;
	
	public PlaylistNodePrimaryIndex(PlaylistNode parent) {
		super(parent);
		audioIds = new ArrayList<Integer>();
		children = new ArrayList<PlaylistNode>();
		this.type = PlaylistNodeType.Internal;
	}
	
	public PlaylistNodePrimaryIndex(PlaylistNode parent, ArrayList<Integer> audioIds, ArrayList<PlaylistNode> children) {
		super(parent);
		this.audioIds = audioIds;
		this.children = children;
		this.type = PlaylistNodeType.Internal;
	}
	
	// GUI Methods - Do not modify
	public ArrayList<PlaylistNode> getAllChildren()
	{
		return this.children;
	}
	
	public PlaylistNode getChildrenAt(Integer index) {return this.children.get(index); }
	
	public Integer audioIdCount()
	{
		return this.audioIds.size();
	}
	public Integer audioIdAtIndex(Integer index) {
		if(index >= this.audioIdCount() || index < 0) {
			return -1;
		}
		else {
			return this.audioIds.get(index);
		}
	}
	
	// Extra functions if needed

	public ArrayList<Integer> getAudioIds() {
		return audioIds;
	}
	public int addId(int id) {
		if (audioIds.size() == 0) {
			audioIds.add(id);
			return 0;
		}
		int i = 0;
		for (; i < audioIds.size(); i++) {
			if (id < audioIds.get(i)) {
				audioIds.add(i, id);
				return i;
			}
		}
		audioIds.add(i, id);
		return i;
	}

	public void murderInfant(int index) {
		children.remove(index);
	}

	public void addChild(int index, PlaylistNode child) {
		children.add(index, child);
	}

}
