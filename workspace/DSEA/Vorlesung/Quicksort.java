public class Quicksort {

	static void swap(int a[],int i,int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	// Partitioniere das Feld a im Indexbereich [l,r], wobei a[r] als Pivotelement x fungiert.
	// Invariante während des Schleifendurchlaufs: a[l],...,a[i] <= x und a[i+1],...,a[j] > x
	// Der Rückgabewert (i+1) ist die neue Position des Pivotelementes x.
	// Laufzeit: O(r-l+1)
	static int partition(int a[],int l,int r) {
		int x = a[r];
		int i = l-1;
		for(int j=l;j<r;j++)
			if (a[j] <= x) {
				i++;
				swap(a,i,j);
			}
		swap(a,i+1,r);
		return i+1;
	}

	// Finde das m-kleinste Element im Indexbereich [l,r]
	// Paritioniere den Bereich mit Hilfe eines zufällig gewählten Pivotelements.
	// p = Indexposition des Pivotelementes nach der Paritionierung
	// Entscheide in welchem Bereich die Suche rekursiv fortgeführt werden muss.
	// Wenn dies im rechten Bereich geschieht, muss dort nun das (m+l-p-1)-kleinste Element gesucht werden.
	// Erwartete Laufzeit: O(n)
	static int select(int a[],int l,int r,int m) {
		int k = l+(int) Math.floor((r-l+1)*Math.random());
		swap(a,k,r);
		int p = partition(a,l,r);
		if (p > m+l) return select(a,l,p-1,m);
		if (p < m+l) return select(a,p+1,r,m+l-p-1);
		return a[p];
	}

	// Partitioniere den zu sortierenden Bereich [l,r] mit Hilfe eines zufällig gewählten Pivotelements.
	// Wende das Verfahren rekursive auf die beiden entstehenden Bereiche an.
	// Worst case Laufzeit: O(n^2)
	// Average case Laufzeit: O(nlog n)
	static void quicksort(int a[],int l,int r) {
		if (l >= r) return;
		int k = l+(int) Math.floor((r-l+1)*Math.random());
		swap(a,k,r);
		int p = partition(a,l,r);
		quicksort(a,l,p-1);
		quicksort(a,p+1,r);
	}

	static public void main(String[] args) {
		int n = 1000000;
		int a[] = new int[n];

		for(int i=0;i<n;i++)
			a[i] = i;

		// Erzeuge eine zufällige Permutation des Eingabefeldes.
		// Jede Permutation hat die gleiche Wahrscheinlichkeit.
		for(int i=n-1;i>0;i--) {
			int j = (int) Math.floor((i+1)*Math.random());
			swap(a,j,i);
		}

		System.out.println("Selection = " + select(a,0,n-1,n/5));
/*
		quicksort(a,0,n-1);

		for(int i=0;i<n-1;i++)
			if (a[i] > a[i+1]) {
				System.out.println("Error");
				return;
			}

		for(int i=0;i<n;i++)
			System.out.println(a[i]+" ");
		System.out.println();
*/
	}
}
