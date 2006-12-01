package org.firstopen.custom.business;

import java.util.Iterator;
import java.util.List;

import org.firstopen.singularity.util.InfrastructureException;

public class Test_Hibernate {

	public Test_Hibernate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws InfrastructureException 
	 */
	public static void main(String[] args) throws InfrastructureException {

		CarterNoteDAO cartersNoteDAO = CarterNoteDAOFactory.create();
		CarterNote carterNote = new CarterNote();
		carterNote.setBookingReference("Test");
		List notes = null;
		try {
			cartersNoteDAO.createCartersNote(carterNote);
		 notes = cartersNoteDAO.getAllCartersNotes();
		} catch (InfrastructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		

		if (notes == null)
			System.out.println("nothing found");
		else
			for (Iterator iter = notes.iterator(); iter.hasNext();) {
				CarterNote element = (CarterNote) iter.next();
				System.out.println("Test" + element.getBookingReference());
			}// end for
	}// end main

}
