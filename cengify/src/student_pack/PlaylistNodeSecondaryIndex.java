package student_pack;

import java.util.ArrayList;

public class PlaylistNodeSecondaryIndex extends PlaylistNode {
	private ArrayList<String> genres;
	private ArrayList<PlaylistNode> children;

	public PlaylistNodeSecondaryIndex(PlaylistNode parent) {
		super(parent);
		genres = new ArrayList<String>();
		children = new ArrayList<PlaylistNode>();
		this.type = PlaylistNodeType.Internal;
	}
	
	public PlaylistNodeSecondaryIndex(PlaylistNode parent, ArrayList<String> genres, ArrayList<PlaylistNode> children) {
		super(parent);
		this.genres = genres;
		this.children = children;
		this.type = PlaylistNodeType.Internal;
	}
	
	// GUI Methods - Do not modify
	public ArrayList<PlaylistNode> getAllChildren()
	{
		return this.children;
	}
	
	public PlaylistNode getChildrenAt(Integer index) {
		
		return this.children.get(index);
	}
	

	public Integer genreCount()
	{
		return this.genres.size();
	}
	
	public String genreAtIndex(Integer index) {
		if(index >= this.genreCount() || index < 0) {
			return "Not Valid Index!!!";
		}
		else {
			return this.genres.get(index);
		}
	}
	// Extra functions if needed

	public int addGenre(String id) {
		if (genres.size() == 0) {
			genres.add(id);
			return 0;
		}
		int i = 0;
		for (; i < genres.size(); i++) {
			if (genres.get(i).compareTo(id) > 0) {
				genres.add(i, id);
				return i;
			}
		}
		genres.add(i, id);
		return i;
	}

	public void slaughterChild(int index) {
		children.remove(index);
	}

	public void addChild(int index, PlaylistNode child) {
		if (index != -1)
			children.add(index, child);
		else
			children.add(child);
	}

	public ArrayList<String> getGenres() {
		return genres;
	}


}
