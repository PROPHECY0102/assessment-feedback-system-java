package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;

public interface Report {
  Object[][] generate(GlobalState state, String filterId);
  String[] getColumns();
  String getTitle();
  
}
