package student_pack;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlaylistTree {
	
	public PlaylistNode primaryRoot;		//root of the primary B+ tree
	public PlaylistNode secondaryRoot;	//root of the secondary B+ tree
	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		primaryRoot.type = PlaylistNodeType.Leaf;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
		secondaryRoot.type = PlaylistNodeType.Leaf;
	}
	
	public void addSong(CengSong song) {
		// TODO: Implement this method
		// add methods to fill both primary and secondary tree

		// root is still a leaf. easiest case.
		if (primaryRoot.type == PlaylistNodeType.Leaf) {

			ArrayList<CengSong> arr = ((PlaylistNodePrimaryLeaf)primaryRoot).getSongs();

			int pos = correctPosLeaf(arr, song);
			((PlaylistNodePrimaryLeaf)primaryRoot).addSong(pos, song);

			// overflow
			if (arr.size() > PlaylistNode.order*2) {
				ArrayList<CengSong> half1 = new ArrayList<CengSong>(arr.subList(0, PlaylistNode.order));
				ArrayList<CengSong> half2 = new ArrayList<CengSong>(arr.subList(PlaylistNode.order, arr.size()));

				primaryRoot = new PlaylistNodePrimaryIndex(null);

				PlaylistNodePrimaryLeaf child1 = new PlaylistNodePrimaryLeaf(primaryRoot, half1);
				PlaylistNodePrimaryLeaf child2 = new PlaylistNodePrimaryLeaf(primaryRoot, half2);

				((PlaylistNodePrimaryIndex) primaryRoot).addId(child2.getSongs().get(0).audioId());
				((PlaylistNodePrimaryIndex) primaryRoot).addChild(0, child1);
				((PlaylistNodePrimaryIndex) primaryRoot).addChild(1, child2);
			}
		// root	is no longer a leaf
		} else {

			// find which leaf the song should be added to
			Boolean notLeaf = true;
			PlaylistNodePrimaryIndex curr = (PlaylistNodePrimaryIndex) primaryRoot;
			PlaylistNodePrimaryLeaf leaf = null;
			while (notLeaf) {
				ArrayList<Integer> arr = curr.getAudioIds();

				PlaylistNode amogus = curr.getAllChildren().get(getChildPosPrimary(arr, song.audioId()));
				amogus.setParent(curr);
				if (amogus.type == PlaylistNodeType.Leaf) {
					notLeaf = false;
					leaf = (PlaylistNodePrimaryLeaf) amogus;
				} else {
					curr = (PlaylistNodePrimaryIndex) amogus;
				}
			}

			// compute overflow stuff

			ArrayList<CengSong> arr = leaf.getSongs();
			int pos = correctPosLeaf(arr, song);
			leaf.addSong(pos, song);

			if (arr.size() > PlaylistNode.order*2) {
				ArrayList<CengSong> half1 = new ArrayList<CengSong>(arr.subList(0, PlaylistNode.order));
				ArrayList<CengSong> half2 = new ArrayList<CengSong>(arr.subList(PlaylistNode.order, arr.size()));

				PlaylistNodePrimaryLeaf child1 = new PlaylistNodePrimaryLeaf(leaf.getParent(), half1);
				PlaylistNodePrimaryLeaf child2 = new PlaylistNodePrimaryLeaf(leaf.getParent(), half2);

				PlaylistNodePrimaryIndex parent = (PlaylistNodePrimaryIndex) leaf.getParent();
				int index = parent.addId(half2.get(0).audioId());
				parent.murderInfant(index);
				parent.addChild(index, child2);
				parent.addChild(index, child1);

				// backtrack through the entire tree to correct any other overflows
				curr = parent;
				parent = (PlaylistNodePrimaryIndex) curr.getParent();
				while (curr.getAllChildren().size()-1 > 2*PlaylistNode.order) {
					ArrayList<PlaylistNode> children = curr.getAllChildren();
					ArrayList<Integer> ids = curr.getAudioIds();
					ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>(children.subList(0, PlaylistNode.order+1));
					ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>(children.subList(PlaylistNode.order+1, children.size()));

					ArrayList<Integer> ids1 = new ArrayList<Integer>(ids.subList(0, PlaylistNode.order));
					ArrayList<Integer> ids2 = new ArrayList<Integer>(ids.subList(PlaylistNode.order + 1, ids.size()));

					PlaylistNodePrimaryIndex branch1 = new PlaylistNodePrimaryIndex(parent, ids1, children1);
					PlaylistNodePrimaryIndex branch2 = new PlaylistNodePrimaryIndex(parent, ids2, children2);

					if (curr == primaryRoot) {
						ArrayList<PlaylistNode> branches = new ArrayList<PlaylistNode>();
						ArrayList<Integer> idsAAA = new ArrayList<Integer>();
						idsAAA.add(ids.get(PlaylistNode.order));
						branches.add((PlaylistNode) branch1);
						branches.add((PlaylistNode) branch2);
						PlaylistNodePrimaryIndex newRoot = new PlaylistNodePrimaryIndex(null, idsAAA, branches);
						primaryRoot = newRoot;
						break;
					} else {


						int index1 = parent.addId(ids.get(PlaylistNode.order));
						parent.murderInfant(index1);
						parent.addChild(index1, branch2);
						parent.addChild(index1, branch1);

						curr = parent;
						parent = (PlaylistNodePrimaryIndex) curr.getParent();
					}
				}
			}
		}


		// Secondary tree

		if (secondaryRoot.type == PlaylistNodeType.Leaf) {

			PlaylistNodeSecondaryLeaf leaf = (PlaylistNodeSecondaryLeaf) secondaryRoot;
			int index = genreIndex(leaf, song.genre());
			leaf.addSong(index, song);


			// implement below here

			// overflow
			if (leaf.genreCount() > PlaylistNode.order*2) {
				ArrayList<ArrayList<CengSong>> half1 = new ArrayList<ArrayList<CengSong>>(leaf.getSongBucket().subList(0, PlaylistNode.order));
				ArrayList<ArrayList<CengSong>> half2 = new ArrayList<ArrayList<CengSong>>(leaf.getSongBucket().subList(PlaylistNode.order, leaf.genreCount()));

				secondaryRoot = new PlaylistNodeSecondaryIndex(null);

				PlaylistNodeSecondaryLeaf child1 = new PlaylistNodeSecondaryLeaf(primaryRoot, half1);
				PlaylistNodeSecondaryLeaf child2 = new PlaylistNodeSecondaryLeaf(primaryRoot, half2);

				((PlaylistNodeSecondaryIndex) secondaryRoot).addGenre(child2.genreAtIndex(0));
				((PlaylistNodeSecondaryIndex) secondaryRoot).addChild(-1, child1);
				((PlaylistNodeSecondaryIndex) secondaryRoot).addChild(-1, child2);
			}
			// root	is no longer a leaf
		} else {

			// find which leaf the genre should be added to
			Boolean notLeaf = true;
			PlaylistNodeSecondaryIndex curr = (PlaylistNodeSecondaryIndex) secondaryRoot;
			PlaylistNodeSecondaryLeaf leaf = null;
			while (notLeaf) {
				ArrayList<String> arr = curr.getGenres();

				PlaylistNode amogus = curr.getAllChildren().get(getChildPosSecondary(arr, song.genre()));
				amogus.setParent(curr);
				if (amogus.type == PlaylistNodeType.Leaf) {
					notLeaf = false;
					leaf = (PlaylistNodeSecondaryLeaf) amogus;
				} else {
					curr = (PlaylistNodeSecondaryIndex) amogus;
				}
			}

			// compute overflow stuff

			ArrayList<ArrayList<CengSong>> arr = leaf.getSongBucket();


			int pos = genreIndex(leaf, song.genre());
			leaf.addSong(pos, song);

			if (arr.size() > PlaylistNode.order*2) {
				ArrayList<ArrayList<CengSong>> half1 = new ArrayList<ArrayList<CengSong>>(arr.subList(0, PlaylistNode.order));
				ArrayList<ArrayList<CengSong>> half2 = new ArrayList<ArrayList<CengSong>>(arr.subList(PlaylistNode.order, arr.size()));

				PlaylistNodeSecondaryLeaf child1 = new PlaylistNodeSecondaryLeaf(leaf.getParent(), half1);
				PlaylistNodeSecondaryLeaf child2 = new PlaylistNodeSecondaryLeaf(leaf.getParent(), half2);

				PlaylistNodeSecondaryIndex parent = (PlaylistNodeSecondaryIndex) leaf.getParent();

				int index = parent.addGenre(child2.genreAtIndex(0));
				parent.slaughterChild(index);
				parent.addChild(index, child2);
				parent.addChild(index, child1);

				// backtrack through the entire tree to correct any other overflows
				curr = parent;
				parent = (PlaylistNodeSecondaryIndex) curr.getParent();
				while (curr.getAllChildren().size()-1 > 2*PlaylistNode.order) {

					ArrayList<PlaylistNode> children = curr.getAllChildren();

					ArrayList<String> genres = curr.getGenres();
					ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>(children.subList(0, PlaylistNode.order+1));
					ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>(children.subList(PlaylistNode.order+1, children.size()));

					ArrayList<String> genres1 = new ArrayList<String>(genres.subList(0, PlaylistNode.order));
					ArrayList<String> genres2 = new ArrayList<String>(genres.subList(PlaylistNode.order + 1, genres.size()));

					PlaylistNodeSecondaryIndex branch1 = new PlaylistNodeSecondaryIndex(parent, genres1, children1);
					PlaylistNodeSecondaryIndex branch2 = new PlaylistNodeSecondaryIndex(parent, genres2, children2);

					if (curr == secondaryRoot) {
						ArrayList<PlaylistNode> branches = new ArrayList<PlaylistNode>();
						ArrayList<String> genresAAA = new ArrayList<String>();
						genresAAA.add(genres.get(PlaylistNode.order));
						branches.add((PlaylistNode) branch1);
						branches.add((PlaylistNode) branch2);
						PlaylistNodeSecondaryIndex newRoot = new PlaylistNodeSecondaryIndex(null, genresAAA, branches);
						secondaryRoot = newRoot;
						break;
					} else {

						int index1 = parent.addGenre(genres.get(PlaylistNode.order));
						parent.slaughterChild(index1);
						parent.addChild(index1, branch2);
						parent.addChild(index1, branch1);

						curr = parent;
						parent = (PlaylistNodeSecondaryIndex) curr.getParent();
					}
				}
			}
		}


		return;
	}

	private void printIndent(int indentations) {
		for (int i = 0; i < indentations; i++) {
			System.out.print("\t");
		}
	}

	public CengSong searchSong(Integer audioId) {
		// TODO: Implement this method
		// find the song with the searched audioId in primary B+ tree
		// return value will not be tested, just print according to the specifications
		int indent = 0;
		PlaylistNode curr = primaryRoot;
		PlaylistNodePrimaryIndex currI = null;
		PlaylistNodePrimaryLeaf currL = null;
		CengSong song = null;
		while (curr.type == PlaylistNodeType.Internal){
			currI = (PlaylistNodePrimaryIndex) curr;
			printIndent(indent);
			System.out.println("<index>");

			ArrayList<Integer> ids = currI.getAudioIds();
			for (int i = 0; i < ids.size(); i++) {
				printIndent(indent);
				System.out.println(ids.get(i));
			}

			int childPos = getChildPosPrimary(ids, audioId);
			curr = currI.getChildrenAt(childPos);

			printIndent(indent);
			System.out.println("</index>");
			indent++;
		}

		currL = (PlaylistNodePrimaryLeaf) curr;
		ArrayList<CengSong> songs = currL.getSongs();
		Boolean found = false;
		for (int i = 0; i < songs.size(); i++) {
			song = songs.get(i);
			if (song.audioId() == audioId) {
				printIndent(indent);
				System.out.println("<data>");
				printIndent(indent);
				System.out.println("<record>" + song.audioId() + "|" + song.genre() + "|" + song.songName() + "|" + song.artist() + "</record>");
				printIndent(indent);
				System.out.println("</data>");
				found = true;
				break;
			}
		}
		if (!found) {
			System.out.println("Could not find " + audioId);
		}

		return song;
	}
	
	public void printPrimaryPlaylist() {
		// TODO: Implement this method
		// print the primary B+ tree in Depth-first order

		printPrimaryHelper(primaryRoot, 0);

		return;
	}

	public void printSecondaryPlaylist() {
		// TODO: Implement this method
		// print the secondary B+ tree in Depth-first order

		printSecondaryHelper(secondaryRoot, 0);

		return;
	}

	private void printPrimaryHelper(PlaylistNode curr, int indent) {
		if (curr.type == PlaylistNodeType.Internal) {
			PlaylistNodePrimaryIndex currI = (PlaylistNodePrimaryIndex) curr;
			printIndent(indent);
			System.out.println("<index>");

			for (int i = 0; i < currI.getAudioIds().size(); i++) {
				printIndent(indent);
				System.out.println(currI.audioIdAtIndex(i));
			}

			printIndent(indent);
			System.out.println("</index>");

			for (int i = 0; i < currI.getAllChildren().size(); i++) {
				printPrimaryHelper(currI.getChildrenAt(i), indent + 1);
			}
		} else {
			PlaylistNodePrimaryLeaf currL = (PlaylistNodePrimaryLeaf) curr;
			printIndent(indent);
			System.out.println("<data>");

			for (int i = 0; i < currL.getSongs().size(); i++) {
				CengSong song = currL.songAtIndex(i);
				printIndent(indent);
				System.out.println("<record>" + song.audioId() + "|" + song.genre() + "|" + song.songName() + "|" + song.artist() + "</record>");
			}

			printIndent(indent);
			System.out.println("</data>");
		}
	}

	private void printSecondaryHelper(PlaylistNode curr, int indent) {
		if (curr.type == PlaylistNodeType.Internal) {
			PlaylistNodeSecondaryIndex currI = (PlaylistNodeSecondaryIndex) curr;
			printIndent(indent);
			System.out.println("<index>");

			for (int i = 0; i < currI.getGenres().size(); i++) {
				printIndent(indent);
				System.out.println(currI.genreAtIndex(i));
			}

			printIndent(indent);
			System.out.println("</index>");

			for (int i = 0; i < currI.getAllChildren().size(); i++) {
				printSecondaryHelper(currI.getChildrenAt(i), indent + 1);
			}
		} else {
			PlaylistNodeSecondaryLeaf currL = (PlaylistNodeSecondaryLeaf) curr;
			printIndent(indent);
			System.out.println("<data>");
			for (int j = 0; j < currL.getSongBucket().size(); j++) {

				printIndent(indent);
				System.out.println(currL.genreAtIndex(j));

				for (int i = 0; i < currL.songsAtIndex(j).size(); i++) {
					CengSong song = currL.songsAtIndex(j).get(i);
					printIndent(indent+1);
					System.out.println("<record>" + song.audioId() + "|" + song.genre() + "|" + song.songName() + "|" + song.artist() + "</record>");
				}

			}
			printIndent(indent);
			System.out.println("</data>");
		}
	}

	// Extra functions if needed

	private int getChildPosPrimary(ArrayList<Integer> arr, Integer id) {
		if (id < arr.get(0)) return 0;
		if (arr.size() == 1) {
			return 1;
		}
		for (int i = 0; i < arr.size()-1; i++) {
			if (id < arr.get(i+1) && id >= arr.get(i)) {
				return i+1;
			}
		}

		return arr.size();
	}

	private int genreIndex(PlaylistNodeSecondaryLeaf leaf, String id) {
		int pos = 0;
		for (int i = 0; i < leaf.getSongBucket().size(); i++) {
			if (leaf.genreAtIndex(i).compareTo(id) < 0) {
				pos++;
			}
			else
				break;
		}
		return pos;
	}

	private int getChildPosSecondary(ArrayList<String> arr, String id) {
		if (arr.get(0).compareTo(id) > 0) return 0;
		if (arr.size() == 1) {
			return 1;
		}
		for (int i = 0; i < arr.size()-1; i++) {
			if (arr.get(i+1).compareTo(id) > 0 && arr.get(i).compareTo(id) <= 0) {
				return i+1;
			}
		}

		return arr.size();
	}

	private int correctPosLeaf(ArrayList<CengSong> arr, CengSong song) {
		int pos = 0;
		for (int i = 0; i < arr.size(); i++) {
			if (song.audioId() > arr.get(i).audioId()) {
				pos++;
			}
			else
				break;
		}
		return pos;
	}

}


