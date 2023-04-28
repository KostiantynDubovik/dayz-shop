class StartSetsPlayersConfig
    {
        void createServerDefaultItems(PlayerBase player) {
            EntityAI stuff = NULL;
            ItemBase stuffCasted = NULL;
            if(pants) {
            stuff = player.GetInventory().CreateInInventory("BandageDressing");
            if (stuff)
            {
                stuffCasted = ItemBase.Cast(stuff);
                stuffCasted.SetQuantity(4);
            }
            stuff = player.GetInventory().CreateInInventory("Apple");
            stuff = player.GetInventory().CreateInInventory("Chemlight_Red");
            stuff = player.GetInventory().CreateInInventory("SodaCan_Cola");
            stuff = player.GetInventory().CreateInInventory("CombatKnife");
            }
        }

        void StartSetsPlayer(PlayerBase player, int use_set_id)
        {
            EntityAI itemCreated = NULL;

            switch( use_set_id )
            {
            case 229:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("M65Jacket_Black");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Gray");
                itemCreated = player.GetInventory().CreateInInventory("Battery9V");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("MilitaryBoots_Black");
                itemCreated = player.GetInventory().CreateInInventory("CowboyHat_Black");
                createServerDefaultItems(player);
                break;
            }
            case 56:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("GorkaPants_BERSORKIR");
                itemCreated = player.GetInventory().CreateInInventory("GorkaEJacket_BERSORKIR");
                itemCreated = player.GetInventory().CreateInInventory("AD_Gloves_Misha");
                itemCreated = player.GetInventory().CreateInInventory("Battery9V");
                itemCreated = player.GetInventory().CreateInInventory("Balaclava3Holes_Green");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("AD_Boots_Misha");
                createServerDefaultItems(player);
                break;
            }
            case 74:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("ShortJeans_Quintessa");
                itemCreated = player.GetInventory().CreateInInventory("TShirt_Quintessa");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BaseballCap_Camo");
                itemCreated = player.GetInventory().CreateInInventory("HikingBootsLow_Black");
                createServerDefaultItems(player);
                break;
            }
            case 75:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("GorkaPants_Brown");
                itemCreated = player.GetInventory().CreateInInventory("GorkaEJacket_Brown");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Brown");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("MilitaryBoots_Brown");
                createServerDefaultItems(player);
                break;
            }
            case 80:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("LegsCover_Improvised_Green");
                itemCreated = player.GetInventory().CreateInInventory("TorsoCover_Improvised_Green");
                itemCreated = player.GetInventory().CreateInInventory("HandsCover_Improvised_Green");
                itemCreated = player.GetInventory().CreateInInventory("FaceCover_Improvised_Green");
                itemCreated = player.GetInventory().CreateInInventory("HeadCover_Improvised_Green");
                itemCreated = player.GetInventory().CreateInInventory("FeetCover_Improvised_Green");
                createServerDefaultItems(player);
                break;
            }
            case 101:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("TC_PolicePantsOrel_Black");
                itemCreated = player.GetInventory().CreateInInventory("TC_PoliceJacketOrel_Black");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 102:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("AD_HikingJacket_Grey");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 103:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Green");
                itemCreated = player.GetInventory().CreateInInventory("AD_HikingJacket_Forest_V3");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Green");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 104:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("GorkaPants_Brown");
                itemCreated = player.GetInventory().CreateInInventory("GorkaEJacket_Brown");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("Ushanka_Black");
                itemCreated = player.GetInventory().CreateInInventory("AviatorGlasses");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 105:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("TC_HunterPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("TC_HuntingJacket_Black");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BaseballCap_Black");
                itemCreated = player.GetInventory().CreateInInventory("AviatorGlasses");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 106:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("HunterPants_Spring");
                itemCreated = player.GetInventory().CreateInInventory("HuntingJacket_Spring");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("BaseballCap_Green");
                itemCreated = player.GetInventory().CreateInInventory("AviatorGlasses");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 107:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("QuiltedJacket_Black");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("TankerHelmet");
                itemCreated = player.GetInventory().CreateInInventory("AviatorGlasses");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 108:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("TC_QuiltedJacket_GreenBlack");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("TankerHelmet");
                itemCreated = player.GetInventory().CreateInInventory("AviatorGlasses");
                itemCreated = player.GetInventory().CreateInInventory("HikingBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 109:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("M65Jacket_Black");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Gray");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("JungleBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 110:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_UA");
                itemCreated = player.GetInventory().CreateInInventory("M65Jacket_UA");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Gray");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("MilitaryBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 111:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("CargoPants_Cam");
                itemCreated = player.GetInventory().CreateInInventory("M65Jacket_Cam");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Gray");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("MilitaryBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            case 112:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("TrackSuitPants_Black");
                itemCreated = player.GetInventory().CreateInInventory("TrackSuitJacket_Black");
                itemCreated = player.GetInventory().CreateInInventory("WorkingGloves_Black");
                itemCreated = player.GetInventory().CreateInInventory("Balaclava3Holes_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("AthleticShoes_Black");
                createServerDefaultItems(player);
                break;
            }
            case 113:
            {
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                itemCreated = player.GetInventory().CreateInInventory("USMCPants_Woodland");
                itemCreated = player.GetInventory().CreateInInventory("USMCJacket_Woodland");
                itemCreated = player.GetInventory().CreateInInventory("OMNOGloves_Gray");
                itemCreated = player.GetInventory().CreateInInventory("BalaclavaMask_Black");
                itemCreated = player.GetInventory().CreateInInventory("TacticalGoggles");
                itemCreated = player.GetInventory().CreateInInventory("MilitaryBoots_Black");
                createServerDefaultItems(player);
                break;
            }
            default:
            {
                array<string> sets = GetPlayersClothing(player);
                if(sets){
                My_Custom_Spawn_Parameters.RemoveAllItems(player);
                foreach(string clothing: sets){
                    player.GetInventory().CreateInInventory(clothing);
                }
                createServerDefaultItems(player);
                }
                break;
            }
            }
        }

        const string ConfigPath = "$profile:DayZShop/DataBase/CustomSets/%1.txt";
        const string ConfigSeparator = "|";

        array<string> GetPlayersClothing(PlayerBase player){
            array<string> Result = new array<string>;
            string FileName = string.Format(ConfigPath, player.GetIdentity().GetPlainId());
            if(FileExist(FileName)){
            FileHandle file = OpenFile(FileName, FileMode.READ);
            if(file){
                string line_content;
                while(FGets(file, line_content) > 0){
                array<string> splited_line = new array<string>;
                line_content.Split(ConfigSeparator, splited_line);

                Result = splited_line;
                break;
                }
            }
            }
            return Result;
        }
    }