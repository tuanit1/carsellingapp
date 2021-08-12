package com.example.autosellingapp.items;

public class EquipmentItem {
   private int equip_id;
   private String equip_name;
   private boolean isChecked;

    public int getEquip_id() {
        return equip_id;
    }

    public void setEquip_id(int equip_id) {
        this.equip_id = equip_id;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public EquipmentItem(int equip_id, String equip_name){
       this.equip_id = equip_id;
       this.equip_name = equip_name;
       this.isChecked = false;
   }

    public EquipmentItem(int equip_id, String equip_name, boolean isChecked) {
        this.equip_id = equip_id;
        this.equip_name = equip_name;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return  equip_name;
    }
}
