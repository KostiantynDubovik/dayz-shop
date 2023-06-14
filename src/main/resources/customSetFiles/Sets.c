class StartSetsPlayersConfig {
	void createServerDefaultItems(PlayerBase player) {
		if(player) {
			ItemBase itemCreated;
			itemCreated = player.GetInventory().CreateInInventory("Headtorch_Black");
			if (itemCreated) {
				itemCreated.GetInventory().CreateInInventory("Battery9V");
			}
			itemCreated = player.GetInventory().CreateInInventory("AD_Conserva_Sgushenka");
			if (itemCreated) {
				player.SetQuickBarEntityShortcut(itemCreated, 0);
			}
			itemCreated = player.GetInventory().CreateInInventory("SodaCan_Cola");
			if (itemCreated) {
				player.SetQuickBarEntityShortcut(itemCreated, 1);
			}
			itemCreated = player.GetInventory().CreateInInventory("BandageDressing");
			if (itemCreated) {
				player.SetQuickBarEntityShortcut(itemCreated, 2);
				itemCreated.SetQuantity(4);
			}
			itemCreated = player.GetInventory().CreateInInventory("CombatKnife");
			if (itemCreated) {
				player.SetQuickBarEntityShortcut(itemCreated, 3);
			}
		}
	}

	void StartSetsPlayer(PlayerBase player, string use_set_id) {
		array<string> sets = GetPlayersClothing(use_set_id);
		if(sets && sets.Count() > 0) {
			My_Custom_Spawn_Parameters.RemoveAllItems(player);
			foreach(string clothing: sets) {
				player.GetInventory().CreateInInventory(clothing);
			}
			createServerDefaultItems(player);
		}
	}

	const string ConfigPath = "$profile:DayZShop/DataBase/CustomSets/%1.txt";
	const string ConfigSeparator = "|";

	array<string> GetPlayersClothing(string use_set_id) {
		string FileName = string.Format(ConfigPath, use_set_id);
		if(!FileExist(FileName)) {
			FileName = string.Format(ConfigPath, "default");
		}
		return ReadLines(FileName);
	}

	array<string> ReadLines(string FileName) {
		array<string> Result = new array<string>;
		FileHandle file = OpenFile(FileName, FileMode.READ);
		if(file) {
			string line_content;
			while(FGets(file, line_content) > 0) {
				array<string> splited_line = new array<string>;
				line_content.Split(ConfigSeparator, splited_line);
				Result = splited_line;
				break;
			}
			CloseFile(file);
		}
		return Result;
	}
}