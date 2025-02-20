package villagegaulois;


import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	private int nbEtalUtilise = 0;


	private static class Marche{
		private Etal etals[];

		public Marche(int nbEtal){
			this.etals = new Etal[nbEtal];
			for (int i = 0; i< nbEtal; i++){
				this.etals[i] = new Etal();
			}
		}

		void utiliserEtal(int indiceEtal, Gaulois vendeur,String produit, int nbProduit){
			this.etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}

		public int trouverEtalLibre(){
			int i = 0;
			for (Etal e : this.etals) {
				if (!e.isEtalOccupe()){
					return i;
				}
				i++;
			}

			return -1;
		}

		public Etal[] trouverEtals(String produit){
			
			int nb = 0;
			for(Etal e : this.etals){
				if(e.contientProduit(produit)){
					nb++;
				}
			}

			Etal[] lisEtal = new Etal[nb];
			int i = 0;
			for(Etal e : this.etals){
				if(e.contientProduit(produit)){
					
					lisEtal[i] = e;
					i++;
				}
			}

			return lisEtal;
		}

		public Etal trouverVendeur(Gaulois gaulois){
			for(Etal e: this.etals){
				if (e.getVendeur() == gaulois){
					return e;
				}
			}

			return null;
		}

		public int trouverNumVendeur(Gaulois gaulois){
			for (int i = 0; i < this.etals.length; i++) {
					if(this.etals[i].getVendeur() == gaulois)
						return i;
			}
			return -1;
		}

		public String afficherMarche(){
			StringBuilder builder = new StringBuilder();

			int etalOcc = 0;
			for(Etal e : this.etals){
				if (e.isEtalOccupe())
					builder.append(e.afficherEtal());
				else
					etalOcc++;
			}
			if(etals.length - etalOcc>0){
				builder.append("Il reste " + (etals.length - etalOcc) + " étals non utilisés dans le marché.\n");
			}
			return builder.toString();
		}
	}

	public Village(String nom, int nbVillageoisMaximum,int tailleMarche) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(tailleMarche);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit){

		int place = this.marche.trouverEtalLibre();

		this.marche.utiliserEtal(place, vendeur, produit, nbProduit);
		nbEtalUtilise++;
		return vendeur.getNom() + " cherche un endroit pour vendre "+ nbProduit+" "+ produit+".\n" 
		+ "Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (place+1)+".\n";
	}

	public String rechercherVendeursProduit(String produit){
		Etal[] etals = this.marche.trouverEtals(produit);
		
		if(etals.length == 0){
			return "Il n'y a pas de vendeur qui propose des "+produit+" au marché.\n";
		}
		
		StringBuilder builder = new StringBuilder();

		if(etals.length == 1)
			builder.append("Seul le vendeur "+ etals[0].getVendeur().getNom() + " propose des "+produit+" au marché.\n");
		else {
			builder.append("Les vendeurs qui proposent des fleurs sont :\n");
			for(Etal e : etals){
				builder.append("- " + e.getVendeur().getNom()+"\n");
			}
		}

		return builder.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur){
		return this.marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur){
		Etal etal = this.marche.trouverVendeur(vendeur);
		if (etal == null)
			return "Malheuresement " + vendeur.toString() + " nous à déjà quitté 😔.";
		nbEtalUtilise --;
		return etal.libererEtal();
	}


/*
 * 
 Le marché du village "le village des irréductibles" possède plusieurs étals :
Assurancetourix vend 5 lyres
Obélix vend 2 menhirs
Panoramix vend 10 fleurs
Il reste 2 étals non utilisés dans le marché.
 */

	public String afficherMarche(){
		StringBuilder builder = new StringBuilder();
		builder.append("Le marché du village \""+ nom + "\" possède plusieurs étals :\n");
		builder.append(this.marche.afficherMarche());

		return builder.toString();
	}
}