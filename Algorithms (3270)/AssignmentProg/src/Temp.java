import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Temp {
    /**
     * Uses binary search to find the index of the first Term in the passed in
     * array which is considered equivalent by a comparator to the given key.
     * This method should not call comparator.compare() more than 1+log n times,
     * where n is the size of a.
     *
     * @param a
     *            - The array of Terms being searched
     * @param key
     *            - The key being searched for.
     * @param comparator
     *            - A comparator, used to determine equivalency between the
     *            values in a and the key.
     * @return The first index i for which comparator considers a[i] and key as
     *         being equal. If no such index exists, return -1 instead.
     */
    public static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
        // TODO: Implement firstIndexOf
        int beg = 0, end = a.length-1;
        int index = -1;
        while (beg <= end) {
            int mid = (beg + end)/2;
            Term cur = a[mid];
            int comparisonResult = comparator.compare(key, cur);
            if (comparisonResult == 0) index = mid;
            if (comparisonResult <= 0) end = mid-1;
            else beg = mid+1;
        }
        return index;
    }

    /**
     * The same as firstIndexOf, but instead finding the index of the last Term.
     *
     * @param a
     *            - The array of Terms being searched
     * @param key
     *            - The key being searched for.
     * @param comparator
     *            - A comparator, used to determine equivalency between the
     *            values in a and the key.
     * @return The last index i for which comparator considers a[i] and key as
     *         being equal. If no such index exists, return -1 instead.
     */
    public static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
        // TODO: Implement lastIndexOf
        int beg = 0, end = a.length-1;
        int index = -1;
        while (beg <= end) {
            int mid = (beg + end)/2;
            Term cur = a[mid];
            int comparisonResult = comparator.compare(key, cur);
            if (comparisonResult == 0) index = mid;
            if (comparisonResult < 0) end = mid-1;
            else beg = mid+1;
        }
        return index;
    }

    /**
     * An Autocompletor supports returning either the top k best matches, or the
     * single top match, given a String prefix.
     *
     * @author Austin Lu
     *
     */
    public interface Autocompletor {

        /**
         * Returns the top k matching terms in descending order of weight. If there
         * are fewer than k matches, return all matching terms in descending order
         * of weight. If there are no matches, return an empty iterable.
         */
        public Iterable<String> topMatches(String prefix, int k);

        /**
         * Returns the single top matching term, or an empty String if there are no
         * matches.
         */
        public String topMatch(String prefix);

        /**
         * Return the weight of a given term. If term is not in the dictionary,
         * return 0.0
         */
        public double weightOf(String term);
    }
    /**
     * Implements Autocompletor by scanning through the entire array of terms for
     * every topKMatches or topMatch query.
     */
    public static class BruteAutocomplete implements Autocompletor {

        Term[] myTerms;

        public BruteAutocomplete(String[] terms, double[] weights) {
            if (terms == null || weights == null)
                throw new NullPointerException("One or more arguments null");
            if (terms.length != weights.length)
                throw new IllegalArgumentException("terms and weights are not the same length");
            myTerms = new Term[terms.length];
            HashSet<String> words = new HashSet<String>();
            for (int i = 0; i < terms.length; i++) {
                words.add(terms[i]);
                myTerms[i] = new Term(terms[i], weights[i]);
                if (weights[i] < 0)
                    throw new IllegalArgumentException("Negative weight "+ weights[i]);
            }
            if (words.size() != terms.length)
                throw new IllegalArgumentException("Duplicate input terms");
        }

        public Iterable<String> topMatches(String prefix, int k) {
            if (k < 0)
                throw new IllegalArgumentException("Illegal value of k:"+k);
            // maintain pq of size k
            PriorityQueue<Term> pq = new PriorityQueue<Term>(k, new Term.WeightOrder());
            for (Term t : myTerms) {
                if (!t.getWord().startsWith(prefix))
                    continue;
                if (pq.size() < k) {
                    pq.add(t);
                } else if (pq.peek().getWeight() < t.getWeight()) {
                    pq.remove();
                    pq.add(t);
                }
            }
            int numResults = Math.min(k, pq.size());
            LinkedList<String> ret = new LinkedList<String>();
            for (int i = 0; i < numResults; i++) {
                ret.addFirst(pq.remove().getWord());
            }
            return ret;
        }

        public String topMatch(String prefix) {
            String maxTerm = "";
            double maxWeight = -1;
            for (Term t : myTerms) {
                if (t.getWeight() > maxWeight && t.getWord().startsWith(prefix)) {
                    maxWeight = t.getWeight();
                    maxTerm = t.getWord();
                }
            }
            return maxTerm;
        }

        public double weightOf(String term) {
            for (Term t : myTerms) {
                if (t.getWord().equalsIgnoreCase(term))
                    return t.getWeight();
            }
            // term is not in dictionary return 0
            return 0;
        }
    }
    /**
     *
     * Using a sorted array of Term objects, this implementation uses binary search
     * to find the top term(s).
     *
     * @author Austin Lu, adapted from Kevin Wayne
     * @author Jeff Forbes
     */
    public static class BinarySearchAutocomplete implements Autocompletor {

        Term[] myTerms;

        /**
         * Given arrays of words and weights, initialize myTerms to a corresponding
         * array of Terms sorted lexicographically.
         *
         * This constructor is written for you, but you may make modifications to
         * it.
         *
         * @param terms
         *            - A list of words to form terms from
         * @param weights
         *            - A corresponding list of weights, such that terms[i] has
         *            weight[i].
         * @return a BinarySearchAutocomplete whose myTerms object has myTerms[i] =
         *         a Term with word terms[i] and weight weights[i].
         * @throws a
         *             NullPointerException if either argument passed in is null
         */
        public BinarySearchAutocomplete(String[] terms, double[] weights) {
            if (terms == null || weights == null)
                throw new NullPointerException("One or more arguments null");
            myTerms = new Term[terms.length];
            for (int i = 0; i < terms.length; i++) {
                myTerms[i] = new Term(terms[i], weights[i]);
            }
            Arrays.sort(myTerms);
        }

        /**
         * Required by the Autocompletor interface. Returns an array containing the
         * k words in myTerms with the largest weight which match the given prefix,
         * in descending weight order. If less than k words exist matching the given
         * prefix (including if no words exist), then the array instead contains all
         * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
         * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
         * 2) should return {"air"}
         *
         * @param prefix
         *            - A prefix which all returned words must start with
         * @param k
         *            - The (maximum) number of words to be returned
         * @return An array of the k words with the largest weights among all words
         *         starting with prefix, in descending weight order. If less than k
         *         such words exist, return an array containing all those words If
         *         no such words exist, reutrn an empty array
         * @throws a
         *             NullPointerException if prefix is null
         */
        public Iterable<String> topMatches(String prefix, int k) {
            if (prefix == null) throw new NullPointerException();
            int f = firstIndexOf(myTerms, new Term(prefix, 0) , new Term.PrefixOrder(prefix.length()));
            int l = lastIndexOf(myTerms, new Term(prefix, 0) , new Term.PrefixOrder(prefix.length()));
            if (l < 0)
                return new ArrayList<String>();
            PriorityQueue<Term> pq = new PriorityQueue<Term>(k, new Term.WeightOrder());
            for (int i = f; i <= l; i++) {
                Term t = myTerms[i];
                if (pq.size() < k) {
                    pq.add(t);
                } else if (pq.peek().getWeight() < t.getWeight()) {
                    pq.remove();
                    pq.add(t);
                }
            }
            int numResults = Math.min(k, pq.size());
            LinkedList<String> ret = new LinkedList<String>();
            for (int i = 0; i < numResults; i++) {
                ret.addFirst(pq.remove().getWord());
            }
            return ret;
        }

        /**
         * Given a prefix, returns the largest-weight word in myTerms starting with
         * that prefix. e.g. for {air:3, bat:2, bell:4, boy:1}, topMatch("b") would
         * return "bell". If no such word exists, return an empty String.
         *
         * @param prefix
         *            - the prefix the returned word should start with
         * @return The word from myTerms with the largest weight starting with
         *         prefix, or an empty string if none exists
         * @throws a
         *             NullPointerException if the prefix is null
         *
         */
        public String topMatch(String prefix) {
            if (prefix == null) throw new NullPointerException();
            int f = firstIndexOf(myTerms, new Term(prefix, 0) , new Term.PrefixOrder(prefix.length()));
            int l = lastIndexOf(myTerms, new Term(prefix, 0) , new Term.PrefixOrder(prefix.length()));
            ArrayList<Term> found = new ArrayList<Term>();
            if (l < 0)
                return "";
            double maxWeight = myTerms[f].getWeight();
            int maxWeightIndex = f;
            for (int i = f+1; i <= l; i++) {
                if (myTerms[i].getWeight() > maxWeight) {
                    maxWeight = myTerms[i].getWeight();
                    maxWeightIndex = i;
                }
            }
            return myTerms[maxWeightIndex].getWord();
        }

        /**
         * Return the weight of a given term. If term is not in the dictionary,
         * return 0.0
         */
        public double weightOf(String term) {
            // TODO complete weightOf
            return 0.0;
        }
    }
    /**
     * General trie/priority queue algorithm for implementing Autocompletor
     *
     * @author Austin Lu
     * @author Jeff Forbes
     */
    public static class Term1 implements Autocompletor {

        /**
         * Root of entire trie
         */
        protected Node myRoot;

        /**
         * Constructor method for TrieAutocomplete. Should initialize the trie
         * rooted at myRoot, as well as add all nodes necessary to represent the
         * words in terms.
         *
         * @param terms
         *            - The words we will autocomplete from
         * @param weights
         *            - Their weights, such that terms[i] has weight weights[i].
         * @throws NullPointerException
         *             if either argument is null
         * @throws IllegalArgumentException
         *             if terms and weights are different weight
         */
        public Term1(String[] terms, double[] weights) {
            if (terms == null || weights == null)
                throw new NullPointerException("One or more arguments null");
            // Represent the root as a dummy/placeholder node
            myRoot = new Node('-', null, 0);

            for (int i = 0; i < terms.length; i++) {
                add(terms[i], weights[i]);
            }
        }

        /**
         * Add the word with given weight to the trie. If word already exists in the
         * trie, no new nodes should be created, but the weight of word should be
         * updated.
         *
         * In adding a word, this method should do the following: Create any
         * necessary intermediate nodes if they do not exist. Update the
         * subtreeMaxWeight of all nodes in the path from root to the node
         * representing word. Set the value of myWord, myWeight, isWord, and
         * mySubtreeMaxWeight of the node corresponding to the added word to the
         * correct values
         *
         * @throws a
         *             NullPointerException if word is null
         * @throws an
         *             IllegalArgumentException if weight is negative.
         */
        private void add(String word, double weight) {
            if (word == null) {
                throw new NullPointerException();
            }
            if (weight < 0) {
                throw new IllegalArgumentException();
            }
            /** create the node representing the word and any intermediary nodes
             that do not already exist */
            Node curr = myRoot;
            if (weight > curr.mySubtreeMaxWeight) {
                curr.mySubtreeMaxWeight = weight;
            }

            // iterate through the trie, adding any nodes that do not already exist
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (!curr.children.containsKey(c)) {
                    curr.children.put(c, new Node(c, curr, weight));
                }
                curr = curr.children.get(c);
                // update the mySubtreeMaxWeight's as necessary
                if (curr.mySubtreeMaxWeight < weight) {
                    curr.mySubtreeMaxWeight = weight;
                }
            }

            // set the last node's isWord and myWord vars
            curr.isWord = true;
            curr.setWord(word);

            /** before we set myWeight of the last node, we must handle the edge case of adding
             *  a word that already fully exist within the tree
             */
            if (curr.mySubtreeMaxWeight == curr.myWeight) {
                curr.setWeight(weight);
                double child_max = weight;
                for (int i = 0; i <= word.length(); i++) {
                    // find the largest mySubTreeMaxWeight of all children of curr
                    for (Node child: curr.children.values()) {
                        if (child.mySubtreeMaxWeight > child_max) {
                            child_max = child.mySubtreeMaxWeight;
                        }
                    }
                    // if the weight of curr is larger than child_max, then change child_max
                    if (child_max < curr.myWeight) {
                        child_max = curr.myWeight;
                    }
                    curr.mySubtreeMaxWeight = child_max;
                    curr = curr.parent;
                }
            } else {
                curr.setWeight(weight);
            }
        }

        /**
         * Required by the Autocompletor interface. Returns an array containing the
         * k words in the trie with the largest weight which match the given prefix,
         * in descending weight order. If less than k words exist matching the given
         * prefix (including if no words exist), then the array instead contains all
         * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
         * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
         * 2) should return {"air"}
         *
         * @param prefix
         *            - A prefix which all returned words must start with
         * @param k
         *            - The (maximum) number of words to be returned
         * @return An Iterable of the k words with the largest weights among all
         *         words starting with prefix, in descending weight order. If less
         *         than k such words exist, return all those words. If no such words
         *         exist, return an empty Iterable
         * @throws a
         *             NullPointerException if prefix is null
         */
        public Iterable<String> topMatches(String prefix, int k) {
            if (prefix == null) {
                throw new NullPointerException();
            }
            if (k == 0) {
                return new ArrayList<String>();
            }

            // make sure the prefix exist in the trie, if not return an empty list
            Node curr = myRoot;
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                if (curr.children.get(c) == null) {
                    // return empty
                    return new ArrayList<String>();
                }
                curr = curr.children.get(c);
            }

            PriorityQueue<Node> pq = new PriorityQueue<>(new Node.ReverseSubtreeMaxWeightComparator());
            pq.add(curr);
            ArrayList<Term> termList = new ArrayList<Term>();

            while (pq.size() > 0) {
                curr = pq.remove();

                if (termList.size() >= k) {
                    Collections.sort(termList, new Term.ReverseWeightOrder());
                    if (termList.get(termList.size()-1).getWeight() > curr.mySubtreeMaxWeight) {
                        break;
                    }
                }
                if (curr.isWord) {
                    termList.add(new Term(curr.getWord(), curr.myWeight));
                }

                // grab all of the children and put them in the pq
                for (Node child: curr.children.values()) {
                    pq.add(child);
                }
            }

            if (termList.size() < k) {
                k = termList.size();
            }

            Collections.sort(termList, new Term.ReverseWeightOrder());
            // puts the terms into an string arraylist
            ArrayList<String> wordList = new ArrayList<String>();

            for (int i = 0; i < k; i++) {
                wordList.add(termList.get(i).getWord());
            }
            return wordList;
        }

        /**
         * Given a prefix, returns the largest-weight word in the trie starting with
         * that prefix.
         *
         * @param prefix
         *            - the prefix the returned word should start with
         * @return The word from with the largest weight starting with prefix, or an
         *         empty string if none exists
         * @throws a
         *             NullPointerException if the prefix is null
         */
        public String topMatch(String prefix) {
            if (prefix == null) {
                throw new NullPointerException();
            }
            // navigate to the node
            Node curr = myRoot;
            for (int i = 0; i < prefix.length(); i++) {
                if (curr.children.get(prefix.charAt(i)) == null) {
                    return "";
                }
                curr = curr.children.get(prefix.charAt(i));
            }

            //navigate down the trie by using the prefix_max val
            double prefix_max = curr.mySubtreeMaxWeight;
            while (prefix_max != curr.myWeight) {
                for (Node n: curr.children.values()) {
                    if (n.mySubtreeMaxWeight == prefix_max) {
                        curr = n;
                    }
                }
            }
            return curr.getWord();
        }

        /**
         * Return the weight of a given term. If term is not in the dictionary,
         * return 0.0
         */
        public double weightOf(String term) {
            // TODO complete weightOf
            return 0.0;
        }

        /**
         * Optional: Returns the highest weighted matches within k edit distance of
         * the word. If the word is in the dictionary, then return an empty list.
         *
         * @param word
         *            The word to spell-check
         * @param dist
         *            Maximum edit distance to search
         * @param k
         *            Number of results to return
         * @return Iterable in descending weight order of the matches
         */
        public Iterable<String> spellCheck(String word, int dist, int k) {
            return null;
        }
    }
}
