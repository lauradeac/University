export class EventDTO {
  eventId?: string;
  name!: string;
  location!: string;
  dateTime!: string;
  category!: string;
  description!: string;
  imageUrl!: string;
  file?: File;
  isEditable?: boolean;

  constructor(
    name: string,
    location: string,
    dateTime: string,
    category: string,
    description: string,
    imageUrl: string,
    eventId?: string,
    file?: File,
    isEditable?: boolean
    ) {
    this.name = name;
    this.location = location;
    this.dateTime = dateTime;
    this.category = category;
    this.description = description;
    this.imageUrl = imageUrl;
    this.eventId = eventId;
    this.file = file
    this.isEditable = false;
  }
}
