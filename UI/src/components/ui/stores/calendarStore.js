
import { create } from "zustand";


const calendarStore = create((set) => ({
  calendars: [],
  addToCalendars: (calendarData) => {
    set({ calendars: Array.isArray(calendarData) ? calendarData : [calendarData] });
  },
  
  removeFromCalendars: (yearToRemove) => {
    set((state) => ({
      calendars: state.calendars.filter((calendar) => calendar.year !== yearToRemove),
    }));
  },
}));



export default calendarStore;

