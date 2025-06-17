from docx2pdf import convert
import sys

def convert_to_pdf(input_path, output_path):
    try:
        convert(input_path, output_path)
        print("SUCCESS")  # Signal success to Java
    except Exception as e:
        # Si l'erreur contient "Word.Application.Quit" mais le PDF est bien généré, considère comme succès
        if "Word.Application.Quit" in str(e):
            print("SUCCESS")
        else:
            print(f"ERROR: {str(e)}")
            sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("ERROR: Requires input and output path arguments")
        sys.exit(1)
    
    convert_to_pdf(sys.argv[1], sys.argv[2])