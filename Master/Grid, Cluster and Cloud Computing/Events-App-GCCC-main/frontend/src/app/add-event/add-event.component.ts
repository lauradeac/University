import {Component, OnInit, ViewChild} from '@angular/core';
import {EventDTO} from "../model/eventDTO.model";
import {EventService} from "../service/event.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.css']
})
export class AddEventComponent implements OnInit {
  event: EventDTO = new EventDTO(
    "Nume eveniment",
    "Locație",
    "2024-06-01T09:00:00",
    "Categorie",
    "Descriere eveniment",
    "URL imagine"
  );

  categories: string[] = [];

  imageFile: File | null = null;

  notificationMessage: string | null = null;

  constructor(private eventService: EventService, private router: Router) {
  }

  ngOnInit(): void {
    this.getCategories();
  }

  @ViewChild('eventForm') eventForm!: NgForm;

  resetForm(): void {
    // Folosește referința la formular pentru a reseta formularul
    if (this.eventForm) {
      this.eventForm.resetForm();
    }
  }

  submitForm(): void {
    if (this.eventForm.invalid || !this.imageFile) {
      console.log("Formularul nu este valid sau nu avem o imagine selectată.");
      return;
    }
    console.log("Evenimentul de adăugat:", this.event);

    this.eventService.addEvent(this.event).subscribe(
      response => {
        console.log("Eveniment adăugat cu succes!");
        this.showNotification("Eveniment adăugat cu succes!");
        this.eventForm.resetForm();
        this.imageFile = null;
        setTimeout(() => {
          this.router.navigate(['/main']);
        }, 3000);
      },
      error => {
        console.error("Eroare la adăugarea evenimentului:", error);
        this.showNotification("Eroare la adăugarea evenimentului.");
      }
    );
  }

  showNotification(message: string): void {
    this.notificationMessage = message;
    setTimeout(() => {
      this.notificationMessage = null;
    }, 2000);
  }

  handleFileInput(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.imageFile = file;
      this.event.file = file;
    }
  }

  getCategories(): void {
    this.eventService.getCategories().subscribe((data: string[]) => {
      this.categories = data;
    });
  }

  cancelForm() {
    // Reset the form
    this.resetForm();

    // Navigate back
    this.router.navigate(['../']);
  }

}
