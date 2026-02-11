package com.apu_afs.Models;

import java.util.List;
import java.util.stream.Collectors;

public class AssignLecturers {

    public static List<Lecturer> getLecturersForModule(Module module) {

        String moduleID = module.getID(); 

        return ModuleLecturer.fetchAll()
                .stream()
                .filter(moduleLecturer -> 
                        moduleLecturer.getModuleID().equals(moduleID)) 
                .map(moduleLecturer -> 
                        User.getUserByMatchingValues(
                                "id", 
                                moduleLecturer.getLecturerID())) 
                .filter(user -> user instanceof Lecturer)
                .map(user -> (Lecturer) user)
                .collect(Collectors.toList());
    }

    public static void assign(Module module, Lecturer lecturer) {

        ModuleLecturer moduleLecturer = new ModuleLecturer();

        moduleLecturer.setModuleID(module.getID());     
        moduleLecturer.setLecturerID(lecturer.getID()); 

        moduleLecturer.save();
    }

    public static void remove(Module module, Lecturer lecturer) {

        ModuleLecturer moduleLecturer = new ModuleLecturer();

        moduleLecturer.setModuleID(module.getID());     
        moduleLecturer.setLecturerID(lecturer.getID()); 

        moduleLecturer.delete();
    }
}
