!**************************************************************************
!****************************** Config.pro ********************************
!
! Ce fichier doit être placé dans le dossier de configuration de CREO
!
! Vous devez définir le chemin d'accès aux différents fichiers ou
! sous-dossiers.
!
! Dans la suite de ce fichier, ce chemin spécifique est : D:\Config-CREO
!
! En fonction de votre installation, sur votre ordinateur personnel,
! vous devez, éventuellement, redéfinir ce chemin d'accès.
!
! Ce chemin d'accès est présent 8 fois dans ce fichier.
!
! ATTENTION à bien respecter les sous-dossiers :
!			ISO, MODELES et Fichiers-TRAILS
!
!****************************** Affichage initial
datum_display yes
axis_display yes
datum_point_display yes
datum_point_tag_display NO
spin_center_display no
display_coordinate_sys NO
open_window_maximized YES

bell no

!****** A DEFINIR *********** Fichier : tree.cfg
mdl_tree_cfg_file C:\Config-CREO\tree.cfg

model_tree_start yes

!****************************** Configurations diverses
model_rename_template no
sketcher_animated_modify no
sketcher_dim_of_revolve_axis yes
sketcher_equal_radii_constr yes
intf_in_use_template_models yes
step_appearance_layers_groups yes

!****************************** Unités : mm - degré - Kg - N
pro_unit_length UNIT_MM
default_ang_units ang_deg
pro_unit_mass UNIT_KILOGRAM
pro_unit_sys MMNS

!****************************** Réglage Tolérances de cotation
tolerance_class MEDIUM
tolerance_standard ISO

!****** A DEFINIR *********** Projection norme européenne : EUROPE.dtl
drawing_setup_file C:\Config-CREO\EUROPE.dtl

!****** A DEFINIR *********** Fichiers TRAILS
trail_dir C:\ProgramData\Config-CREO\Fichiers-TRAILS

!****** A DEFINIR *********** Chemins d'accès aux répertoires de travail
search_path_file C:\ProgramData\Config-CREO\search.pro

!****** A DEFINIR *********** Chemin de la table de tolérance
tolerance_table_dir C:\Config-CREO\ISO\

!****************************** Affichage des tolérances
tol_display no

!****************************** Configuration de l'esquisse : grille non affichée
sketcher_disp_grid no

!********* A DEFINIR *********** Gabarits (Modèles) par défaut
template_designasm C:\Config-CREO\MODELES\assemblage.asm.1
template_solidpart C:\Config-CREO\MODELES\piece.prt.1
template_drawing C:\Config-CREO\MODELES\dessin.drw.1

!****************************** LAYERS *********************************
create_numbered_layers NO
def_layer LAYER_AXIS 01_AXES
def_layer LAYER_QUILT 05_SURFACES
def_layer LAYER_DATUM 03_PLANS
def_layer LAYER_POINT 02_POINTS
def_layer LAYER_CURVE 04_COURBES
def_layer LAYER_CSYS 06_REPERES
def_layer LAYER_DRIVEN_DIM 07_DIM_PILOTEES
def_layer LAYER_HOLE_FEAT 08_PERCAGES
def_layer LAYER_ROUND_FEAT 09_ARRONDIS
def_layer LAYER_CHAMFER_FEAT 10_CHANFREINS
blank_layer EFFACER


edge_display_quality very_high

!************* FIN de config.pro ****************************************
!************************************************************************
display_axis_tags yes
